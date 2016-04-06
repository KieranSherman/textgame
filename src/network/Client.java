package network;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
	
	private String hostName;
	private int portNumber;
	private Socket socket;
	
	public Client() {
		this.hostName = "localhost";
		this.portNumber = 4444;
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
	public void run() {
		try {
			socket = new Socket(hostName, portNumber);
			
			System.out.println("connection to "+socket.getInetAddress());
			
			socket.close();
		} catch (IOException e) {
			System.err.println("client unable to connect");
		}
	}

}
