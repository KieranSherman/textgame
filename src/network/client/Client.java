package network.client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import network.Adapter;
import network.client.util.ClientReceiver;
import network.client.util.ClientSender;
import network.packet.Packet;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

/*
 * Class models a threadable client in a network
 */
public class Client extends Thread {
	
	private String hostName;			//ip address
	private int portNumber;				//port number
	
	private Socket socket;				//TCP socket	
	
	private ClientReceiver clientReceiver;
	private ClientSender clientSender;
	
	public Client() {
		this.hostName = "localhost";
		this.portNumber = 9999;
	}
	
	public Client(String hostName) {
		this();
		this.hostName = hostName;
	}
	
	public Client(String hostName, int portNumber) {
		this(hostName);
		this.portNumber = portNumber;
	}
	
	@Override
	/*
	 * connects to server on hostName, portNumber; opens the input/output streams;
	 * sends a confirmation login packet; receives and parses packets 
	 */
	public void run() {
		Adapter adapter = null;
		try {
			adapter = Resources.getAdapter();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		Logger logger = null;
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		String error = null;
		try {
			socket = new Socket(hostName, portNumber);
		} catch (IOException e) {
			error = "client unable to connect";
			System.err.println(error);
			logger.appendText(error, Color.RED);
			adapter.destroyClient();
		}
				
		try {
			clientSender = new ClientSender(socket);
		} catch (IOException e) {
			error = "client sender unable to initialize";
			System.err.println(error);
			logger.appendText(error, Color.RED);
			adapter.destroyClient();
		}
		
		try {
			clientReceiver = new ClientReceiver(socket, adapter);
		} catch (IOException e) {
			error = "client receiver unable to initialize";
			System.err.println(error);
			logger.appendText(error, Color.RED);
			adapter.destroyClient();
		}
		
		// start a new thread to receive and handle incoming packets
		Thread cReceiver_T = new Thread(clientReceiver);
		cReceiver_T.start();
		
		// wait until close call
		synchronized(this) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
			
		clientSender.close();
		clientReceiver.close();
		
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("error closing client socket");
		}
		
		System.err.println("client disconnected");
		logger.appendText("[you have been disconnected]", Color.GRAY);
		
		adapter.destroyClient();
	}

	public void sendPacket(Packet packet) {
		if(clientSender != null)
			clientSender.sendPacket(packet);
	}
	
	public void disconnect() {
		synchronized(this) {
			this.notifyAll();
		}
	}
	
}
