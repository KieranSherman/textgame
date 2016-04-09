package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.bridge.ClientBridge;

public class Client extends Thread {
	
	private String hostName;
	private int portNumber;
	private Socket socket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	private ClientBridge clientBridge;
	
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
	
	public void setBridge(ClientBridge clientBridge) {
		this.clientBridge = clientBridge;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(hostName, portNumber);
			
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(socket.getInputStream());

			new Thread(new ServerListener()).start();
		} catch (IOException e) {
			System.err.println("client unable to connect");
		}
	}
	
	public void writeObject(Object obj) {
		try {
			sOutput.writeObject(obj);
			sOutput.reset();
			sOutput.flush();
		} catch (Exception e) {
			System.err.print("error sending object: "+obj);
		}
	}
	
	public <T> T read() {
		if(clientBridge != null)
			return clientBridge.readObject();
		else
			return readObject();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T readObject() {
		try {
			return (T) sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
					String msg = (String) read();
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
