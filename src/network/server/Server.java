package network.server;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ui.components.notifications.NotificationUI;
import network.Adapter;
import network.User;
import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.packet.types.Packet02Disconnect;
import network.packet.types.Packet03Message;
import network.server.util.ServerConnection;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class models a threadable server in a network
 */
public class Server extends Thread {
	
	private int localhostConnections;	//whether localhost is connected
	private int portNumber;				//port number
	private Socket clientSocket;		//socket the client connects from
	private ServerSocket serverSocket;	//socket the client connects to
	
	private Logger logger;
	
	private ArrayList<ServerConnection> serverConnections;
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
		
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	/*
	 * opens a server; waits for incoming connections;
	 * sends confirmation login packet; receives and parses packets
	 */
	public void run() {
		super.setName("ServerThread-Main");
		
		serverConnections = new ArrayList<ServerConnection>();
		
		Adapter adapter = null;
		try {
			adapter = Resources.getAdapter();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		String error = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			logger.appendText("[server started at "+InetAddress.getLocalHost().getHostAddress()+
					":"+serverSocket.getLocalPort()+"]", Color.CYAN);
		} catch (IOException e) {
			error = "[server unable to initialize]";
			System.err.println(error);
			logger.appendText(error, Color.RED);
			adapter.destroyServer();
		}
		
		new Thread("ServerThread-ServerListenerThread") {
			public void run() {
				while(true) {
					try {
						clientSocket = serverSocket.accept();
					} catch (IOException e) {
						System.err.println("server unable to connect with client");
						break;
					}
					
					openConnection();
				}
			}
		}.start();
		
		synchronized(this) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("error closing server socket");
		}
		
		System.err.println("server closed");
		logger.appendText("[server closed]", Color.GRAY);
		
		adapter.destroyServer();
	}
	
	private void openConnection() {
		ServerConnection serverConnection = new ServerConnection(this, clientSocket);
		new Thread(serverConnection).start();
		
		serverConnections.add(0, serverConnection);

		NotificationUI.queueNotification("CLIENT CONNECTED", 5000, null, true);
	}
	
	public void removeConnection(ServerConnection serverConnection) {
		int index = serverConnections.indexOf(serverConnection);
		
		if(index != -1) {
			if(isLocalHost(serverConnection.getConnectedAddress()))
				localhostConnections--;
			
			serverConnections.remove(index);
			logger.appendText("[client disconnected]", Color.GRAY);
			
			NotificationUI.queueNotification("CLIENT DISCONNECTED", 5000, null, true);
			
			System.out.println("*REMOVED CONNECTION @ "+serverConnection.getConnectedAddress()+"*");
		}
	}
	
	/*
	 * Sends a packet to a client at hostAddress
	 */
	public void sendPacketToClient(Packet packet, String hostAddress) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress)) {
				sConnection.sendPacket(packet);
				break;
			}
	}
	
	/*
	 * Sends a packet to all connected clients
	 */
	public void sendPacketToAllClients(Packet packet) {
		Formatter.formatServer(packet);
		
		for(ServerConnection sConnection : serverConnections)
			sConnection.sendPacket(packet);
	}
	
	/*
	 * Relays a packet to all clients except for client at hostAddress
	 */
	public void sendPacketToAllOtherClients(Packet packet, String hostAddress) {
		Formatter.deconstruct(packet);
		User user = getUserAtPacket(packet).getUser();
		
		System.out.println("USER WHO SENT PACKET: "+user.getUsername());
		System.out.println("USER IS AT: "+user.getHostAddress());
		System.out.println("HOST ADDRESS: "+hostAddress);
		
		for(ServerConnection sConnection : serverConnections) {
			System.out.println("CONNECTION @ "+sConnection.getConnectedAddress());
			if(!sConnection.getConnectedAddress().equals(hostAddress)) {
				Formatter.formatUsername(packet, user.getUsername());
				sConnection.sendPacket(packet);
			}
		}
		
		Formatter.construct(packet);
	}
	
	/*
	 * Registers a user provided they're not banned, their username isn't taken, and they're not already connected
	 */
	public void registerUser(Packet01Login packet) {
		String hostAddress = packet.getHostAddress();
		String username = packet.getUsername();
		
		logger.appendText("[attempting to register user ("+username+") at "+hostAddress+"]", Color.GRAY);
				
		if(checkBanList(hostAddress)) {
			disconnectUser(hostAddress, "[you have been banned]");
			return;
		}
		
		if(checkConnected(hostAddress)) {
			disconnectUser(hostAddress, "[already connected from another host]");
			return;
		}
		
		if(checkUsername(username)) {
			disconnectUser(hostAddress, "[username already taken]");
			return;
		}
		
		addUser(username, hostAddress);
	}
	
	/*
	 * Sets a server connection's user
	 */
	private void addUser(String username, String hostAddress) {
		ServerConnection userConnection = null;
		
		for(ServerConnection sConnection : serverConnections) {
			if(sConnection.getConnectedAddress().equals(hostAddress) || isLocalHost(hostAddress)) {
				logger.appendText("[adding user: "+username+"]", Color.GREEN);
				sConnection.setUser(new User(hostAddress, username));
				userConnection = sConnection;
				break;
			}
		}
		
		for(ServerConnection sConnection : serverConnections)
			if(userConnection != null && !sConnection.equals(userConnection)) {
				sConnection.sendPacket(new Packet03Message("["+userConnection.getUser().getUsername()+" has connected]"));
				sendPacketToClient(new Packet03Message("["+sConnection.getUser().getUsername()+" is here]"), userConnection.getConnectedAddress());
			}
	}
	
	/*
	 * Disconnects a user at hostAddress with a message
	 */
	private void disconnectUser(String hostAddress, String message) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress) || 
					(isLocalHost(hostAddress) && isLocalHost(sConnection.getConnectedAddress()))) {
				sConnection.sendPacket(new Packet02Disconnect(message));
				sConnection.close();
				break;
			}
	}
	
	/*
	 * Checks the ban list for hostAddress
	 */
	private boolean checkBanList(String hostAddress) {
		for(String address : Resources.BANLIST)
			if(address.equals(hostAddress)) {
				logger.appendText("user at ("+address+") is banned", Color.RED);
				return true;
			}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if username is already taken
	 */
	private boolean checkUsername(String username) {
		if(usernameTaken(username)) {
			logger.appendText("username ("+username+") already taken", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if connection request is already connected
	 */
	private boolean checkConnected(String hostAddress) {
		if(alreadyConnected(hostAddress)) {
			logger.appendText("user at "+hostAddress+" already connected", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/*
	 * Checks to see if hostAddress matches the localhost
	 */
	private boolean isLocalHost(String hostAddress) {
		try {
			return (hostAddress.equals(InetAddress.getLocalHost().getHostAddress()) || hostAddress.equals("127.0.0.1"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if connection request is already connected.
	 * Used in checkConnected()
	 */
	private boolean alreadyConnected(String hostAddress) {
		System.out.println("TESTING IF "+hostAddress+" IS ALREADY CONNECTED");
		
		if(isLocalHost(hostAddress)) {
			System.out.println("\t*IS LOCALHOST*");
			
			if(++localhostConnections > 1)
				return true;
		}
		
		int connections = 0;
		
		for(ServerConnection sConnection : serverConnections) {
			String address = sConnection.getConnectedAddress();
			System.out.println("CONNECTION @: "+address);
			
			if(address.equals(hostAddress))
				connections++;
		}
		
		return connections > 1;
	}
	
	/*
	 * Checks connected clients to see if username is already taken.
	 * Used in checkUsername()
	 */
	private boolean usernameTaken(String username) {
		for(ServerConnection sConnection : serverConnections) {
			User user = sConnection.getUser();
			
			if(user != null)
				if(user.getUsername().equalsIgnoreCase(username))
					return true;
		}
		
		return false;
	}
	
	/*
	 * Returns the user who sent packet
	 */
	private ServerConnection getUserAtPacket(Packet packet) {
		for(ServerConnection sConnection : serverConnections) {
			if(sConnection.getUser().getHostAddress().equals(packet.getHostAddress()))
				return sConnection;
		}
		
		return null;
	}
	
	/*
	 * Notifies the server to close
	 */
	public void close() {
		synchronized(this) {
			this.notifyAll();
		}
	}
	
}
