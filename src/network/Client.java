package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {
	
	private String hostName;
	private int portNumber;
	private Socket socket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
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
			
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			
			Thread serverListener = new Thread(new ServerListener());
			serverListener.start();
			
			sendMessage("hello server");
			
			socket.close();
		} catch (IOException e) {
			System.err.println("client unable to connect");
		}
	}
	
	public void sendMessage(String message) {
		try {
			sOutput.writeObject(message);
		} catch (Exception e) {
			System.err.print("error sending message: "+message);
		}
	}
	
	public void disconnect() {
		try {
			if(sInput != null)
				sInput.close();
			
			if(sOutput != null)
				sOutput.close();
			
			if(socket != null)
				socket.close();
		} catch (Exception e) {
			System.err.println("error disconnecting");
		} finally {
			System.out.println("you have disconnected");
		}
	}
	
	private class ServerListener extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					String msg = (String)sInput.readObject();
					System.out.println(msg);
					System.out.print("> ");
				} catch (Exception e) {
					System.out.println("server has closed the connection");
					break;
				}
			}
		}
	}

}
