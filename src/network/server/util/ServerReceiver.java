package network.server.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import network.Adapter;
import network.packet.Packet;
import network.util.NetworkTypes;

/**
 * Class listens on a socket for incoming packets.
 * 
 * @author kieransherman
 * 
 */
public class ServerReceiver extends Thread {

	private ServerConnection serverConnection;
	private ObjectInputStream sInput;
	
	/**
	 * Creates a new thread to listen on the given socket.
	 * 
	 * @param serverConnection the serverConnection 
	 * @param clientSocket the socket to send to
	 * @throws IOException the socket is closed
	 */
	public ServerReceiver(ServerConnection serverConnection, Socket clientSocket) throws IOException {
		this.serverConnection = serverConnection;
		this.sInput = new ObjectInputStream(clientSocket.getInputStream());
	}
	
	@Override
	public void run() {
		super.setName("ServerThread-ServerReceiverThread_@"+serverConnection.getConnectedAddress());
		
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
			
			Adapter.parsePacket(NetworkTypes.SERVER, packet);
			
		} while (true);
		
		serverConnection.close();
	}
	
	/**
	 * Returns the packet from the input stream.
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
	
	/**
	 * Closes the input stream.
	 */
	protected void close() {
		if(sInput != null)
			try {
				sInput.close();
			} catch (IOException e) {
				System.err.println("error closing client input stream");
			}
	}
	
}
