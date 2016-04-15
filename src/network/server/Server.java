package network.server;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import network.Adapter;
import network.User;
import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.server.util.ServerConnection;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class models a threadable server in a network
 */
public class Server extends Thread {
	
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
			error = "server unable to initialize";
			System.err.println(error);
			logger.appendText(error, Color.RED);
			adapter.destroyServer();
		}
		
		new Thread() {
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
		
		// wait until call from close
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
		
		serverConnections.add(serverConnection);
	}
	
	public void removeConnection(ServerConnection serverConnection) {
		int index = serverConnections.indexOf(serverConnection);
		
		if(index != -1) {
			serverConnections.remove(index);
			logger.appendText("[client disconnected]", Color.GRAY);
		}
	}
	
	public void sendPacket(Packet packet) {
		packet = Formatter.formatServer(packet);
		
		for(ServerConnection sConnection : serverConnections)
			sConnection.sendPacket(packet);
	}
	
	public void registerUser(Packet01Login packet) {
		String hostAddress = packet.getHostAddress();
		String username = packet.getUsername();
		
		logger.appendText("attempting to register user at "+hostAddress, Color.GRAY);
		
		for(ServerConnection sConnection : serverConnections) {
			try {
				if(sConnection.getConnectedAddress().equals(hostAddress) || 
						(sConnection.getConnectedAddress().equals("127.0.0.1") 
								&& hostAddress.equals(InetAddress.getLocalHost().getHostAddress())) ) {
					logger.appendText("adding user: "+username, Color.GREEN);
					sConnection.setUser(new User(hostAddress, username));
				}
			} catch (UnknownHostException e) {
				System.err.println("error registering user");
			}
		}
	}
	
	public void sendPacketToAllClients(Packet packet) {
		packet = Formatter.deconstruct(packet);
		ServerConnection packetHost = getUser(packet);
		
		for(ServerConnection sConnection : serverConnections)
			if(!packetHost.equals(sConnection)) {
				packet = Formatter.formatUsername(packet, packetHost.getUser().getUsername());
				sConnection.sendPacket(packet);
			}
		
		packet = Formatter.construct(packet);
	}
	
	 private ServerConnection getUser(Packet packet) {
		for(ServerConnection sConnection : serverConnections)
			if(sConnection.getUser().getHostAddress().equals(packet.getHostAddress()))
				return sConnection;
		
		return null;
	}
	
	public void close() {
		synchronized(this) {
			this.notifyAll();
		}
	}
	
}
