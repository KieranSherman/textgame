package network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import network.Adapter;
import network.NetworkTypes;
import network.packet.Packet;

public class ClientReceiver extends Thread {
	
	private Adapter adapter;
	private ObjectInputStream sInput;
	
	public ClientReceiver(Socket socket, Adapter adapter) throws IOException {
		this.sInput = new ObjectInputStream(socket.getInputStream());
		this.adapter = adapter;
	}
	
	@Override
	public void run() {
		do {
			Packet packet = null;
			
			if(sInput != null)
				try {
					packet = getPacket();
				} catch (IOException e) {
					break;
				}
			
			if(packet == null)
				continue;
			
			if(adapter != null)
				adapter.parsePacket(NetworkTypes.CLIENT, packet);
		} while (true);
		
		System.err.println("client receiver closing connection to server");
	}
	
	/*
	 * returns the packet from the input stream
	 */
	protected Packet getPacket() throws IOException {
		Packet packet = null;
		try {
			packet = (Packet) sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return packet;
	}
	
	/*
	 * closes the input stream
	 */
	protected void close() {
		if(sInput != null)
			try {
				sInput.close();
			} catch (IOException e) {
				System.err.println("error closing client input stream");
			}
		
		if(adapter != null)
			adapter = null;
	}

}
