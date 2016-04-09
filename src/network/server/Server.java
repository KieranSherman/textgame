package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

public class Server extends Thread {
	
	private int portNumber;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	private Logger logger;
	
	public Server() {
		this.portNumber = 9999;
	}
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	@Override
	public void run() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
			clientSocket = serverSocket.accept();
			
			sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(clientSocket.getInputStream());
			
			while(true) {
				String msg = readObject();
				
				if(msg.equalsIgnoreCase("logout"))
					break;
				
				logger.appendText(msg);
			}
			
			serverSocket.close();
		} catch (Exception e) {
			System.err.println("server unable to initialize");
		} finally {
			close();
		}
	}
	
	protected void writeObject(Object object) {
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
	
	@SuppressWarnings("unchecked")
	protected <T> T readObject() {
		try {
			return (T) sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
		
		System.out.println("server closed");
	}
	
}
