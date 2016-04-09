package network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

public class Client extends Thread {
	
	private String hostName;
	private int portNumber;
	private Socket socket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	private Logger logger;
	
	private boolean connected;
	
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
	
	@Override
	public void run() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			socket = new Socket(hostName, portNumber);
			connected = true;
			
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(socket.getInputStream());

			while(true) {
				String msg = readObject();
				logger.appendText(msg);
			}
		} catch (IOException e) {
			System.err.println("client unable to connect");
		} finally {
			disconnect();
		}
	}
	
	protected void writeObject(Object obj) {
		try {
			sOutput.writeObject(obj);
			sOutput.reset();
			sOutput.flush();
		} catch (Exception e) {
			System.err.print("error sending object: "+obj);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T readObject() {
		Object obj = null;
		try {
			obj = sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			disconnect();
		} 
		
		return (T) obj;
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
			System.exit(1);
		} 
		
		if(connected)
			System.out.println("client disconnected");
		
		connected = false;
		
	}
	
}
