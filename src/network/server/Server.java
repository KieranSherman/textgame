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
import network.upnp.UPnPGateway;
import util.Action;
import util.Resources;
import util.out.DeveloperLogger;
import util.out.Formatter;
import util.out.DefaultLogger;

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
	
	/**
	 * Initializes the server
	 */
	public static void initialize(int portNumber) {
		Server.portNumber = portNumber;
		Server.initialized = true;
	}
	
	/**
	 * Starts the server
	 */
	public static void startServer() {
		serverThread = new Thread("ServerThread-Main") {
			public void run() {
				Server.run();
			}
		};
		
		serverThread.start();
	}
	
	/**
	 * opens a server; waits for incoming connections;
	 * sends confirmation login packet; receives and parses packets
	 */
	private static void run() {
		String error = null;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			error = "[server unable to initialize]";
			System.err.println(error);
			DefaultLogger.appendColoredText(error, Color.RED);
			Adapter.destroyServer();
			return;
		}
		
		UPnPGateway.openGatewayAtPort(portNumber);
		DefaultLogger.appendColoredText("[server started at "+UPnPGateway.getMappedAddress()+":"+serverSocket.getLocalPort()+"]", Color.CYAN);
		
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
	
	/**
	 * Opens a connection at the client's socket
	 */
	private static void openConnection(Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket);
		new Thread(serverConnection).start();
		
		DeveloperLogger.appendText("clientSocket temporary connection address: "+serverConnection.getConnectedAddress());
		
		serverConnections.add(0, serverConnection);
	}
	
	/**
	 * Removes a serverConnection
	 */
	public static void removeConnection(ServerConnection serverConnection) {
		int index = serverConnections.indexOf(serverConnection);
		
		if(index != -1) {
			if(isLocalHost(serverConnection.getConnectedAddress()))
				localHostConnections--;
			
			serverConnections.remove(index);
			
			String username = "client";
			if(serverConnection.getUser() != null)
				username = serverConnection.getUser().getUsername();
			
			DefaultLogger.appendColoredText("["+username+" disconnected]", Color.GRAY);
			NotificationUI.queueNotification(username+" DISCONNECTED", 1500, null, true);
			StatusUI.removeStatus(username);
			
			DeveloperLogger.appendColoredText("*Removed connection @ "+serverConnection.getConnectedAddress()+"*", Color.RED);
		}
	}
	
	/**
	 * Sends a packet to a client at hostAddress
	 */
	public static void sendPacketToClient(Packet packet, String hostAddress) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress)) {
				sConnection.sendPacket(packet);
				break;
			}
	}
	
	/**
	 * Sends a packet to all connected clients
	 */
	public static void sendPacketToAllClients(Packet packet) {
		Formatter.formatServer(packet);
		
		for(ServerConnection sConnection : serverConnections)
			sConnection.sendPacket(packet);
	}
	
	/**
	 * Relays a packet to all clients except for client at hostAddress
	 */
	public static void sendPacketToAllOtherClients(Packet packet, String hostAddress) {
		Formatter.deconstruct(packet);
		User user = getServerConnectionAtAddress(hostAddress).getUser();
		
		for(ServerConnection sConnection : serverConnections)
			if(!sConnection.getConnectedAddress().equals(hostAddress) && !isLocalHost(sConnection.getConnectedAddress())) {
				Formatter.formatUsername(packet, user.getUsername());
				sConnection.sendPacket(packet);
			}
		
		Formatter.construct(packet);
	}
	
	/**
	 * Attempts to register a user provided they're not banned, 
	 * their username isn't taken, they're not already connected,
	 * and the maximum number of clients hasn't been reached
	 */
	public static void registerUser(Packet01Login packet) {
		String hostAddress = packet.getHostAddress();
		String username = packet.getUsername();
		
		DeveloperLogger.appendColoredText("[attempting to register user ("+username+") at "+hostAddress+"]", Color.GRAY);
		
		if(checkUsername(username)) {
			disconnectUser(hostAddress, "[username already taken]");
			return;
		}
		
		serverConnections.get(0).setUser(new User(hostAddress, username));
		
		
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
		
		addUser(hostAddress, username);
		DefaultLogger.appendColoredText("[registering user: "+username+"]", Color.GREEN);
	}
	
	/**
	 * Sets a server connection's user
	 */
	private synchronized static void addUser(String hostAddress, String username) {
		ServerConnection userConnection = serverConnections.get(0);
		
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
		
	/**
	 * Disconnects a user at hostAddress with a message
	 */
	private synchronized static void disconnectUser(String hostAddress, String message) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress) || isLocalHost(sConnection.getConnectedAddress())) {
				sConnection.sendPacket(new Packet02Disconnect(message));
				sConnection.close();
				break;
			}
	}

	/**
	 * Checks to see if the maximum number of clients has joined
	 */
	private static boolean checkMaximum() {
		return clientConnectionMaximum != -1 && serverConnections.size() >= clientConnectionMaximum;
	}
	
	/**
	 * Checks the ban list for hostAddress
	 */
	private static boolean checkBanList(String hostAddress) {
		for(String address : Resources.BANLIST)
			if(address.equals(hostAddress)) {
				DefaultLogger.appendColoredText("[user at ("+address+") is banned]", Color.RED);
				return true;
			}
		
		for(String address : Resources.tempBanList)
			if(address.equals(hostAddress)) {
				DefaultLogger.appendColoredText("[user at ("+address+") is banned]", Color.RED);
				return true;
			}
		
		return false;
	}
	
	/**
	 * Checks connected clients to see if username is already taken
	 */
	private static boolean checkUsername(String username) {
		if(usernameTaken(username)) {
			DefaultLogger.appendColoredText("[username ("+username+") already taken]", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks connected clients to see if connection request is already connected
	 */
	private static boolean checkConnected(String hostAddress) {
		if(alreadyConnected(hostAddress)) {
			DefaultLogger.appendColoredText("[user at ("+hostAddress+") already connected]", Color.RED);
			return true;
		}
		
		return false;
	}
	
	/**
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
	
	/**
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
	
	/**
	 * Returns the user who sent packet
	 */
	private static ServerConnection getServerConnectionAtAddress(String hostAddress) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getConnectedAddress().equals(hostAddress) || isLocalHost(sConnection.getConnectedAddress()))
				return sConnection;
		
		return null;
	}
	
	/**
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
	
	/**
	 * Add hostAddress to the ban list
	 */
	private static void addToBanList(String hostAddress) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File("src/files/reference/lists/banlist.txt"), true);
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
	
	/**
	 * Notifies the server to close
	 */
	public static void close() {
		UPnPGateway.disconnect();

		try {
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			System.err.println("error closing server socket");
		}
		
		serverThread = null;

		DefaultLogger.appendColoredText("[server closed]", Color.GRAY);
	}
	
	/**
	 * Returns if the server is initialized
	 */
	public static boolean isInitialized() {
		return Server.initialized;
	}
	
	/**
	 * Returns if the server is running
	 */
	public static boolean isRunning() {
		return serverThread != null;
	}
	
}
