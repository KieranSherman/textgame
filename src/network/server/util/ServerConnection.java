package network.server.util;

import java.io.IOException;
import java.net.Socket;

import network.User;
import network.packet.Packet;
import network.server.Server;

public class ServerConnection extends Thread {
	
	private Socket clientSocket;
	private ServerSender serverSender;
	private ServerReceiver serverReceiver;
	private User user;
	private String connectedAddress;
	
	public ServerConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.connectedAddress = clientSocket.getRemoteSocketAddress().toString();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
		this.connectedAddress = user.getHostAddress();
	}
	
	public String getConnectedAddress() {
		return connectedAddress;
	}
	
	@Override
	public void run() {
		super.setName("ServerThread-ServerConnectionThread_@"+clientSocket.getRemoteSocketAddress().toString());
		
		openConnection();
	}
	
	public void openConnection() {
		try {
			serverSender = new ServerSender(clientSocket);
		} catch (IOException e) {
			System.err.println("server sender unable to initialize");
		}
		
		try {
			serverReceiver = new ServerReceiver(this, clientSocket);
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
		
		Server.removeConnection(this);
		
		serverSender.close();
		serverReceiver.close();
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
