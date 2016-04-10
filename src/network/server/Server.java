package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import network.Adapter;
import network.NetworkTypes;
import network.packet.Packet;
import network.packet.PacketTypes;
import network.packet.types.Packet01Login;
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
	
	private ObjectInputStream sInput;	//input stream
	private ObjectOutputStream sOutput;	//output stream
	
	private boolean open;				//whether or not the server is open
	
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
		Adapter adapter = null;
		Logger logger = null;
		try {
			adapter = Resources.getAdapter();
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
			logger.appendText("server started at "+InetAddress.getLocalHost()+":"+serverSocket.getLocalPort());
			clientSocket = serverSocket.accept();
			open = true;
			
			sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(clientSocket.getInputStream());
			
			sendPacket(new Packet01Login("you have connected to ["+clientSocket.getInetAddress()+":"+clientSocket.getPort()+"]"));
			
			while(true) {
				Packet packet = getPacket();
				
				if(packet.getType() == PacketTypes.DISCONNECT)
					break;
				
				if(packet != null)
					adapter.parsePacket(NetworkTypes.SERVER, packet);
			}
			
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("server encountered problems");
		} finally {
			close();
		}
	}
	
	/*
	 * sends a packet over the output stream
	 */
	public void sendPacket(Packet packet) {
		if(packet == null)
			return;
		
		if(clientSocket == null) {
			System.err.println("no client connected");
			return;
		}
		
		if(!clientSocket.isConnected()) {
			close();
			return;
		}
		
		try {
			sOutput.writeObject(packet);
			sOutput.reset();
			sOutput.flush();
		} catch (IOException e) {
			System.err.println("error sending packet: ["+packet+", "+packet.getData()+"]");
			packet = null;
		}
	}
	
	/*
	 * returns the packet from the input stream
	 */
	protected Packet getPacket() {
		Packet packet = null;
		try {
			packet = (Packet) sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			close();
		}
		
		return packet;
	}
	
	/*
	 * closes the server
	 */
	public void close() {
		try {
			if(sOutput != null)
				sOutput.close();
			
			if(sInput != null)
				sInput.close();
			
			if(clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			System.err.println("fatal error closing server");
			System.exit(1);
		}
		
		if(open)
			System.out.println("server closed");

		open = false;
	}
	
}
