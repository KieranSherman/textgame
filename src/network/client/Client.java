package network.client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import network.Adapter;
import network.client.util.ClientReceiver;
import network.client.util.ClientSender;
import network.packet.Packet;
import sound.SoundPlayer;
import util.out.DefaultLogger;

/**
 * Class consists exclusively of static methods and models a threadable client in a network.
 * 
 * @author kieransherman
 * @see #initialize(String, int)
 * @see #startClient()
 * @see #disconnect()
 * 
 */
public class Client {
	
	private static String username;
	private static String hostAddress;
	private static int portNumber;
	private static boolean initialized;
	
	private static Socket clientSocket;
	
	private static ClientReceiver clientReceiver;
	private static ClientSender clientSender;
	
	private static Thread clientThread;
	
	// Prevent object instantiation
	private Client() {}

	/**
	 * Initializes the client at {@code [hostAddress:port]}.
	 * 
	 * @param hostAddress the address to connect to.
	 * @param portNumber the port to connect to.
	 */
	public static void initialize(String hostAddress, int portNumber) {
		Client.hostAddress = hostAddress;
		Client.portNumber = portNumber;
		Client.initialized = true;
	}
	
	/**
	 * Sets the client's username.
	 * 
	 * @param username the username.
	 */
	public static void setUsername(String username) {
		Client.username = username;
	}
	
	/**
	 * Return the client's username.
	 * 
	 * @return the username.
	 */
	public static String getUsername() {
		return username;
	}
	
	/**
	 * Starts a new client thread.
	 */
	public static void startClient() {
		clientThread = new Thread("ClientThread-Main") {
			public void run() {
				Client.run();
			}
		};
		
		clientThread.start();
	}
	
	/**
	 * Connects to server on [hostAddress:portNumber].  Opens the input and output streams.
	 * Sends a confirmation login packet.  Starts new client receiver thread.
	 */
	private static void run() {
		String error = null;
		try {
			clientSocket = new Socket(hostAddress, portNumber);
		} catch (IOException e) {
			error = "[client unable to connect]";
			System.err.println(error);
			SoundPlayer.play("error");
			DefaultLogger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
			return;
		}
				
		try {
			clientSender = new ClientSender(clientSocket, username);
		} catch (IOException e) {
			error = "[client sender unable to initialize]";
			System.err.println(error);
			DefaultLogger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
			return;
		}
		
		try {
			clientReceiver = new ClientReceiver(clientSocket);
		} catch (IOException e) {
			error = "[client receiver unable to initialize]";
			System.err.println(error);
			DefaultLogger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
			return;
		}
		
		Thread cReceiver_T = new Thread(clientReceiver);
		cReceiver_T.start();
	}

	/**
	 * Sends a packet to the server.
	 * 
	 * @param packet the packet to send.
	 */
	public static void sendPacket(Packet packet) {
		if(clientSender != null)
			clientSender.sendPacket(packet);
	}
	
	/**
	 * Disconnects the client from the server.
	 */
	public static void disconnect() {
		if(clientSender != null)
			clientSender.close();
		
		if(clientReceiver != null)
			clientReceiver.close();
		
		try {
			if(clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			System.err.println("error closing client socket");
		}
		
		clientThread = null;
		
		DefaultLogger.appendColoredText("[you have been disconnected]", Color.GRAY);
	}
	
	/**
	 * Returns whether or not the client has been initialized.
	 * 
	 * @return whether the client has been initialized.
	 */
	public static boolean isInitialized() {
		return Client.initialized;
	}
	
	/**
	 * Returns whether or not the client thread is running.
	 * 
	 * @return whether the client thread is running.
	 */
	public static boolean isRunning() {
		return clientThread != null;
	}
	
}
