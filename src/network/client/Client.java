package network.client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import network.Adapter;
import network.client.util.ClientReceiver;
import network.client.util.ClientSender;
import network.packet.Packet;
import sound.SoundPlayer;
import util.out.Logger;

/*
 * Class models a threadable client in a network
 */
public class Client {
	
	private static String username;
	private static String hostName;			//ip address
	private static int portNumber;				//port number
	private static boolean initialized;
	
	private static Socket socket;				//TCP socket	
	
	private static ClientReceiver clientReceiver;
	private static ClientSender clientSender;
	
	private static Thread clientThread;
	
	private Client() {}

	public static void initialize(String hostName, int portNumber) {
		Client.hostName = hostName;
		Client.portNumber = portNumber;
		Client.initialized = true;
	}
	
	public static void setUsername(String username) {
		Client.username = username;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static void startClient() {
		clientThread = new Thread("ClientThread-Main") {
			public void run() {
				Client.run();
			}
		};
		
		clientThread.start();
	}
	
	/*
	 * connects to server on hostName, portNumber; opens the input/output streams;
	 * sends a confirmation login packet; receives and parses packets 
	 */
	private static void run() {
		String error = null;
		try {
			socket = new Socket(hostName, portNumber);
		} catch (IOException e) {
			error = "[client unable to connect]";
			System.err.println(error);
			SoundPlayer.play("error");
			Logger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
		}
				
		try {
			clientSender = new ClientSender(socket, username);
		} catch (IOException e) {
			error = "[client sender unable to initialize]";
			System.err.println(error);
			Logger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
		}
		
		try {
			clientReceiver = new ClientReceiver(socket);
		} catch (IOException e) {
			error = "[client receiver unable to initialize]";
			System.err.println(error);
			Logger.appendColoredText(error, Color.RED);
			Adapter.destroyClient();
		}
		
		Thread cReceiver_T = new Thread(clientReceiver);
		cReceiver_T.start();
	}

	public static void sendPacket(Packet packet) {
		if(clientSender != null)
			clientSender.sendPacket(packet);
	}
	
	public static void disconnect() {
		clientSender.close();
		clientReceiver.close();
		
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("error closing client socket");
		}
		
		Logger.appendColoredText("[you have been disconnected]", Color.GRAY);
	}
	
	public static boolean isInitialized() {
		return Client.initialized;
	}
	
	public static boolean isRunning() {
		return clientThread != null;
	}
	
}
