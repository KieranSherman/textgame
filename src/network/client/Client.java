package network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.Adapter;
import network.NetworkTypes;
import network.packet.Packet;
import network.packet.PacketTypes;
import network.packet.types.Packet03Text;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;

public class Client extends Thread {
	
	private String hostName;
	private int portNumber;
	private Socket socket;
	
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	
	
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
		Adapter adapter = null;
		try {
			adapter = Resources.getAdapter();
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
			
			adapter.sendPacket(new Packet03Text("client has connected from ["+socket.getInetAddress()+":"+socket.getPort()+"]"));

			while(true) {
				Packet packet = (Packet) readObject();
				
				if(packet.getType() == PacketTypes.DISCONNECT)
					break;
				
				adapter.parsePacket(NetworkTypes.SERVER, packet);
			}
		} catch (IOException e) {
			System.err.println("client unable to connect");
		} finally {
			disconnect();
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
	
	@SuppressWarnings("unchecked")
	protected <T> T readObject() {
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
