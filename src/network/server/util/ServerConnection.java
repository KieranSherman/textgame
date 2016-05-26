package network.server.util;

import java.io.IOException;
import java.net.Socket;

import network.User;
import network.packet.Packet;
import network.server.Server;

/**
 * Class models a connection with a client.
 * 
 * @author kieransherman
 *
 */
public class ServerConnection extends Thread {
	
	private Socket clientSocket;
	private ServerSender serverSender;
	private ServerReceiver serverReceiver;
	private User user;
	private String connectedAddress;
	
	/**
	 * Creates a connection with a client socket.
	 * 
	 * @param clientSocket the client socket.
	 */
	public ServerConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.connectedAddress = clientSocket.getRemoteSocketAddress().toString();
	}

	/**
	 * Returns the user of the connection.
	 * 
	 * @return the user.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user of the connection.
	 * 
	 * @param user the user.
	 */
	public void setUser(User user) {
		this.user = user;
		this.connectedAddress = user.getHostAddress();
	}
	
	/**
	 * Returns the address the client is connected from.
	 * 
	 * @return the connected address.
	 */
	public String getConnectedAddress() {
		return connectedAddress;
	}
	
	@Override
	public void run() {
		super.setName("ServerThread-ServerConnectionThread_@"+clientSocket.getRemoteSocketAddress().toString());
		
		openConnection();
	}
	
	/**
	 * Opens a connection with the client socket.
	 */
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
		
		Thread sReceiver_T = new Thread(serverReceiver);
		sReceiver_T.start();
	}
	
	/**
	 * Closes the server connection.
	 */
	public void close() {
		Server.removeConnection(this);
		
		serverSender.close();
		serverReceiver.close();
	}
	
	/**
	 * Sends a packet to the client.
	 * 
	 * @param packet the packet to send.
	 */
	public void sendPacket(Packet packet) {
		if(serverSender != null)
			serverSender.sendPacket(packet);
	}
	
}
