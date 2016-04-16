package network.server.util;

import java.io.IOException;
import java.net.Socket;

import network.Adapter;
import network.User;
import network.packet.Packet;
import network.server.Server;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;

public class ServerConnection extends Thread {
	
	private Server server;
	private Socket clientSocket;
	private ServerSender serverSender;
	private ServerReceiver serverReceiver;
	private User user;
	
	public ServerConnection(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public void run() {
		openConnection();
	}
	
	public void openConnection() {
		Adapter adapter = null;
		try {
			adapter = Resources.getAdapter();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			serverSender = new ServerSender(clientSocket);
		} catch (IOException e) {
			System.err.println("server sender unable to initialize");
		}
		
		try {
			serverReceiver = new ServerReceiver(this, clientSocket, adapter);
		} catch (IOException e) {
			System.err.println("server receiver unable to initialize");
		}
		
		// start a new thread to receive and handle incoming packets
		Thread sReceiver_T = new Thread(serverReceiver);
		sReceiver_T.start();
		
		synchronized(this) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
		
		server.removeConnection(this);
		
		serverSender.close();
		serverReceiver.close();
	}
	
	public String getConnectedAddress() {
		String address = clientSocket.getRemoteSocketAddress().toString();
		return address.substring(address.indexOf('/')+1, address.indexOf(':'));
	}
	
	public void close() {
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	public void sendPacket(Packet packet) {
		if(serverSender != null)
			serverSender.sendPacket(packet);
	}
	
}
