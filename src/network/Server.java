package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private int portNumber;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	public Server() {
		portNumber = 4444;
	}
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			clientSocket = serverSocket.accept();
			
			System.out.println("connection from "+clientSocket.getInetAddress());
			
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("server unable to initialize");
		}
	}
	
}
