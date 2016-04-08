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
	
	public static void main(String [] args) {
		new Client().start();
	}
	
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
			socket = new Socket(hostName, portNumber);
			
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sOutput.flush();
			
			sInput = new ObjectInputStream(socket.getInputStream());

			new Thread(new ServerListener()).start();
			writeObject("logout");
			
		} catch (IOException e) {
			System.err.println("client unable to connect");
		}
	}
	
	public void writeObject(Object object) {
		try {
			sOutput.writeObject(object);
			sOutput.reset();
			sOutput.flush();
		} catch (Exception e) {
			System.err.print("error sending object: "+object);
		}
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
			System.out.println("Client is listening for server");
			
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
