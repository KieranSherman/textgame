package network.client;

import java.io.IOException;
import java.net.Socket;

import network.Adapter;
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
		
		try {
			socket = new Socket(hostName, portNumber);
		} catch (IOException e) {
			System.err.println("client unable to connect");
		}
				
		try {
			clientSender = new ClientSender(socket);
		} catch (IOException e) {
			System.err.println("client sender unable to initialize");
		}
		
		try {
			clientReceiver = new ClientReceiver(socket, adapter);
		} catch (IOException e) {
			System.err.println("client receiver unable to initialize");
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
		
		System.err.println("client disconnected");
		logger.appendText("you have been disconnected");
		
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
