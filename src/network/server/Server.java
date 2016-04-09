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
import network.packet.types.Packet03Text;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

public class Server extends Thread {
	
	private int portNumber;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	private boolean open;
	
	public Server() {
		this.portNumber = 9999;
	}
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	@Override
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
			
			sendPacket(new Packet03Text("you have connected from ["+clientSocket.getInetAddress()+":"+clientSocket.getPort()+"]"));
			
			while(true) {
				Packet packet = getPacket();
				
				if(packet.getType() == PacketTypes.DISCONNECT)
					break;
				
				adapter.parsePacket(NetworkTypes.SERVER, packet);
			}
			
			serverSocket.close();
		} catch (Exception e) {
			System.err.println("server encountered problems");
		} finally {
			close();
		}
	}
	
	public void sendPacket(Packet packet) {
		System.out.println("SERVER: attempting to send packet: "+packet.getData());
		
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
			System.err.println("error sending message: "+packet);
		}
	}
	
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
	
	public void close() {
		try {
			if(sOutput != null)
				sOutput.close();
			
			if(sInput != null)
				sInput.close();
			
			if(clientSocket != null)
				clientSocket.close();
		} catch (Exception e) {
			System.err.println("error closing server");
			System.exit(1);
		}
		
		if(open)
			System.out.println("server closed");

		open = false;
	}
	
}
