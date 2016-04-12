package network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import network.Adapter;
import network.packet.Packet;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

/*
 * Class models a threadable server in a network
 */
public class Server extends Thread {
	
	private int portNumber;				//port number
	private Socket clientSocket;		//socket the client connects from
	private ServerSocket serverSocket;	//socket the client connects to
	
	private ArrayList<ServerConnection> serverConnections;
	
	public Server() {
		this.portNumber = 9999;
	}
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	@Override
	/*
	 * opens a server; waits for incoming connections;
	 * sends confirmation login packet; receives and parses packets
	 */
	public void run() {
		serverConnections = new ArrayList<ServerConnection>();

		Logger logger = null;
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		Adapter adapter = null;
		try {
			adapter = Resources.getAdapter();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
			logger.appendText("server started at "+InetAddress.getLocalHost().getHostAddress()+":"+serverSocket.getLocalPort());
		} catch (IOException e) {
			System.err.println("server unable to initialize");
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
		
		System.err.println("server closed");
		logger.appendText("server closed");
		adapter.destroyServer();
	}
	
	private void openConnection() {
		ServerConnection serverConnection = new ServerConnection(this, clientSocket);
		new Thread(serverConnection).start();
		
		serverConnections.add(serverConnection);
	}
	
	public void removeConnection(ServerConnection serverConnection) {
		int index = serverConnections.indexOf(serverConnection);
		
		if(index != -1) 
			serverConnections.remove(index);
	}
	
	public void sendPacket(Packet packet) {
		for(ServerConnection sConnection : serverConnections)
			sConnection.sendPacket(packet);
	}
	
	public void close() {
		synchronized(this) {
			this.notifyAll();
		}
	}
	
}
