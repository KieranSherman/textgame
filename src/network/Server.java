package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import network.bridge.ServerBridge;

public class Server extends Thread {
	
	private int portNumber;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	private ServerBridge serverBridge;
	
	public Server() {
		this.portNumber = 9999;
	}
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}

	public void setBridge(ServerBridge serverBridge) {
		this.serverBridge = serverBridge;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			clientSocket = serverSocket.accept();
			
			sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(clientSocket.getInputStream());
			
			while(true) {
				String msg = read();
				
				if(msg.equalsIgnoreCase("logout"))
					break;
			}
			
			serverSocket.close();
		} catch (Exception e) {
			System.err.println("server unable to initialize");
		} finally {
			close();
		}
	}
	
	public void writeObject(Object object) {
		if(!clientSocket.isConnected()) {
			close();
			return;
		}
		
		try {
			sOutput.writeObject(object);
			sOutput.reset();
			sOutput.flush();
		} catch (IOException e) {
			System.err.println("error sending message: "+object);
		}
	}
	
	private <T> T read() {
		if(serverBridge != null)
			return serverBridge.readObject();
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
	
	private void close() {
		try {
			if(sOutput != null)
				sOutput.close();
			
			if(sInput != null)
				sInput.close();
			
			if(clientSocket != null)
				clientSocket.close();
		} catch (Exception e) {
			System.err.println("error closing server");
		}
	}
	
}
