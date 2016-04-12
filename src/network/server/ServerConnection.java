package network.server;

import java.io.IOException;
import java.net.Socket;

import network.Adapter;
import network.packet.Packet;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;

public class ServerConnection extends Thread {
	
	private Server server;
	private Socket clientSocket;
	private ServerSender serverSender;
	private ServerReceiver serverReceiver;
	
	public ServerConnection(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
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
	
	public void close() {
		System.out.println("closing server connection");
		
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	public void sendPacket(Packet packet) {
		if(serverSender != null)
			serverSender.sendPacket(packet);
	}

}
