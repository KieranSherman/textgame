package network.server;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ui.components.display.notification.NotificationUI;
import main.ui.components.display.status.StatusUI;
import main.ui.components.popup.PopupUI;
import network.Adapter;
import network.User;
import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.packet.types.Packet02Disconnect;
import network.packet.types.Packet03Message;
import network.server.util.ServerConnection;
import util.Action;
import util.Resources;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class models a threadable server in a network
 */
public class Server {
	
	protected static int localHostConnections;	//whether localhost is connected
	protected static int localHostMaximum;		//localhost maximum connections
	protected static int sameClientMaximum;			//client maximum connections from same address
	protected static int clientConnectionMaximum;	//maximum number of clients
	protected static int portNumber;			//port number
	protected static boolean initialized;

	protected static ServerSocket serverSocket;	//socket the client connects to
	protected static Thread serverThread;
	
	protected static volatile ArrayList<ServerConnection> serverConnections;
	
	static {
		localHostConnections = 0;
		localHostMaximum = 1;
		sameClientMaximum = 1;
		clientConnectionMaximum = -1;
		portNumber = 9999;
		initialized = false;
		serverConnections = new ArrayList<ServerConnection>();
	}
	
	private Server() {}
	
	public static void initialize(int portNumber) {
		Server.portNumber = portNumber;
		Server.initialized = true;
	}
	
	public static void startServer() {
		serverThread = new Thread("ServerThread-Main") {
			public void run() {
				Server.run();
			}
		};
		
		serverThread.start();
	}
	
	/*
	 * opens a server; waits for incoming connections;
	 * sends confirmation login packet; receives and parses packets
	 */
	private static void run() {
		String error = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			Logger.appendColoredText("[server started at "+InetAddress.getLocalHost().getHostAddress()+
					":"+serverSocket.getLocalPort()+"]", Color.CYAN);
		} catch (IOException e) {
			error = "[server unable to initialize]";
			System.err.println(error);
			Logger.appendColoredText(error, Color.RED);
			Adapter.destroyServer();
			return;
		}
		
		new Thread("ServerThread-ServerListenerThread") {
			public void run() {
				Socket clientSocket = null;
				while(true) {
					try {
						clientSocket = serverSocket.accept();
					} catch (IOException e) {
						System.err.println("server unable to connect with client");
						break;
					}
					
					openConnection(clientSocket);
				}
			}
		}.start();
	}
	
	private static void openConnection(Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket);
		new Thread(serverConnection).start();
		
		serverConnections.add(0, serverConnection);
	}
	
	public static void removeConnection(ServerConnection serverConnection) {
		int index = serverConnections.indexOf(serverConnection);
		
		if(index != -1) {
			if(isLocalHost(serverConnection.getConnectedAddress()))
				localHostConnections--;
			
			serverConnections.remove(index);
			
			String username = "client";
			if(serverConnection.getUser() != null)
				username = serverConnection.getUser().getUsername();
			
			Logger.appendColoredText("["+username+" disconnected]", Color.GRAY);
			NotificationUI.queueNotification(username+" DISCONNECTED", 1500, null, true);
			StatusUI.removeStatus(username);
			
			System.out.println("*REMOVED CONNECTION @ "+serverConnection.getConnectedAddress()+"*");
		}
	}
	
	/*
	 * Sends a packet to a client at hostAddress
	 */
	public static void sendPacketToClient(Packet packet, String hostAddress) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress)) {
				sConnection.sendPacket(packet);
				break;
			}
	}
	
	/*
	 * Sends a packet to all connected clients
	 */
	public static void sendPacketToAllClients(Packet packet) {
		Formatter.formatServer(packet);
		
		for(ServerConnection sConnection : serverConnections)
			sConnection.sendPacket(packet);
	}
	
	/*
	 * Relays a packet to all clients except for client at hostAddress
	 */
	public static void sendPacketToAllOtherClients(Packet packet, String hostAddress) {
		Formatter.deconstruct(packet);
		User user = getServerConnectionAtPacket(hostAddress).getUser();
		
		System.out.println("USER WHO SENT PACKET: "+user.getUsername());
		System.out.println("\tUSER IS AT: "+user.getHostAddress());
		System.out.println("\tHOST ADDRESS: "+hostAddress);
		
		for(ServerConnection sConnection : serverConnections) {
			System.out.println("\t\tCONNECTION @ "+sConnection.getConnectedAddress());
			if(!sConnection.getConnectedAddress().equals(hostAddress)) {
				Formatter.formatUsername(packet, user.getUsername());
				sConnection.sendPacket(packet);
			}
		}
		
		Formatter.construct(packet);
	}
	
	/*
	 * Attempts to register a user provided they're not banned, 
	 * their username isn't taken, and they're not already connected
	 */
	public static void registerUser(Packet01Login packet) {
		String hostAddress = packet.getHostAddress();
		String username = packet.getUsername();
		
		Logger.appendColoredText("[attempting to register user ("+username+") at "+hostAddress+"]", Color.GRAY);
			
		if(checkMaximum()) {
			disconnectUser(hostAddress, "[server full]");
			return;
		}
		
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
	private static void addUser(String username, String hostAddress) {
		ServerConnection userConnection = null;
		
		for(ServerConnection sConnection : serverConnections) {
			if(sConnection.getConnectedAddress().equals(hostAddress) || isLocalHost(sConnection.getConnectedAddress())) {
				userConnection = sConnection;
				
				Logger.appendColoredText("[adding user: "+username+"]", Color.GREEN);
				userConnection.setUser(new User(hostAddress, username));
				break;
			}
		}
		
		if(userConnection == null)
			System.err.println("FATAL ERROR << userConnection NULL");
		
		for(ServerConnection sConnection : serverConnections)
			if(userConnection != null && !sConnection.equals(userConnection))
				sendPacketToClient(new Packet03Message("["+sConnection.getUser().getUsername()+" is here]"), userConnection.getConnectedAddress());
		
		Packet03Message connected = new Packet03Message("["+username+" has connected]");
		
		Formatter.construct(connected);
		sendPacketToAllOtherClients(connected, userConnection.getConnectedAddress());
		
		NotificationUI.queueNotification(username+" CONNECTED", 1500, null, true);
		
		Action kick = new Action() {
			@Override
			public void execute() {
				PopupUI.promptChoice(username, new String[] {"BAN", "KICK", "CANCEL"});
				String choice = PopupUI.getData();
				
				if(choice.equalsIgnoreCase("cancel"))
					return;
				
				if(choice.equalsIgnoreCase("ban"))
					addToBanList(hostAddress);
				
				disconnectUser(hostAddress, null);
			}
			
			public String toString() {
				return username;
			}
		};
		
		StatusUI.addStatus(kick);
	}
	
	/*
	 * Disconnects a user at hostAddress with a message
	 */
	private static void disconnectUser(String hostAddress, String message) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress) || isLocalHost(sConnection.getConnectedAddress())) {
				sConnection.sendPacket(new Packet02Disconnect(message));
				sConnection.close();
				break;
			}
	}

	/*
	 * 
	 */
	private static boolean checkMaximum() {
		return clientConnectionMaximum != -1 && serverConnections.size() >= clientConnectionMaximum;
	}
	
	/*
	 * Checks the ban list for hostAddress
	 */
	private static boolean checkBanList(String hostAddress) {
		for(String address : Resources.BANLIST)
			if(address.equals(hostAddress)) {
				Logger.appendColoredText("[user at ("+address+") is banned]", Color.RED);
				return true;
			}
		
		for(String address : Resources.tempBanList)
			if(address.equals(hostAddress)) {
				Logger.appendColoredText("[user at ("+address+") is banned]", Color.RED);
				return true;
			}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if username is already taken
	 */
	private static boolean checkUsername(String username) {
		if(usernameTaken(username)) {
			Logger.appendColoredText("[username ("+username+") already taken]", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if connection request is already connected
	 */
	private static boolean checkConnected(String hostAddress) {
		if(alreadyConnected(hostAddress)) {
			Logger.appendColoredText("[user at ("+hostAddress+") already connected]", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/*
	 * Checks connected clients to see if connection request is already connected.
	 * Used in checkConnected()
	 */
	private static boolean alreadyConnected(String hostAddress) {
		System.out.println("TESTING IF "+hostAddress+" IS ALREADY CONNECTED");
		
		if(isLocalHost(hostAddress)) {
			System.out.println("\t*IS LOCALHOST* (MAX "+localHostMaximum+")");
			
			if(++localHostConnections > localHostMaximum)
				return true;
		}
		
		int connections = 0;
		
		for(ServerConnection sConnection : serverConnections) {
			String address = sConnection.getConnectedAddress();
			System.out.println("CONNECTION @: "+address);
			
			if(address.equals(hostAddress))
				connections++;
		}
		
		return connections > sameClientMaximum;
	}
	
	/*
	 * Checks connected clients to see if username is already taken.
	 * Used in checkUsername()
	 */
	private static boolean usernameTaken(String username) {
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
	private static ServerConnection getServerConnectionAtPacket(String hostAddress) {
		for(ServerConnection sConnection : serverConnections) 
			if(sConnection.getConnectedAddress().equals(hostAddress))
				return sConnection;
		
		return null;
	}
	
	/*
	 * Checks to see if hostAddress matches the localhost
	 */
	private static boolean isLocalHost(String hostAddress) {
		try {
			return (hostAddress.equals(InetAddress.getLocalHost().getHostAddress()) 
					|| hostAddress.equals("127.0.0.1"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static void addToBanList(String hostAddress) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File("src/files/reference/banlist.txt"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(hostAddress+"\n");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Resources.tempBanList.add(hostAddress);
	}
	
	/*
	 * Notifies the server to close
	 */
	public static void close() {
		try {
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			System.err.println("error closing server socket");
		}
		
		serverThread = null;
		
		Logger.appendColoredText("[server closed]", Color.GRAY);
	}
	
	public static boolean isInitialized() {
		return Server.initialized;
	}
	
	public static boolean isRunning() {
		return serverThread != null;
	}
	
}
