package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private int portNumber;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
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
			
			sInput = new ObjectInputStream(clientSocket.getInputStream());
			sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			
			while(true) {
				String msg = (String)sInput.readObject();
				
				if(msg.equals("logout"))
					break;
				else
					sendMessage("server received: "+msg);
			}
			
			serverSocket.close();
		} catch (Exception e) {
			System.err.println("server unable to initialize");
		} finally {
			close();
		}
	}
	
	private void sendMessage(String message) {
		if(!clientSocket.isConnected()) {
			close();
			return;
		}
		
		try {
			sOutput.writeObject(message);
		} catch (IOException e) {
			System.err.println("error sending message: "+message);
		}
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
