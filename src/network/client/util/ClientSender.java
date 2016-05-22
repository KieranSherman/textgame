package network.client.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import network.packet.Packet;
import network.packet.types.Packet01Login;

/**
 * Class sends packets over a socket.
 * 
 * @author kieransherman
 * 
 */
public class ClientSender {
	
	private ObjectOutputStream sOutput;
	
	/**
	 * Creates a new object to send packets with.
	 * 
	 * @param socket the socket to send over.
	 * @param username the username.
	 * @throws IOException the socket is closed.
	 */
	public ClientSender(Socket socket, String username) throws IOException {
		sOutput = new ObjectOutputStream(socket.getOutputStream());
		sOutput.flush();
		
		sendPacket(new Packet01Login("[client has connected from "+InetAddress.getLocalHost().getHostAddress()+"]", username));
	}
	
	/**
	 * Sends a packet over the socket output stream.
	 * 
	 * @param packet the packet to send.
	 */
	public void sendPacket(Packet packet) {
		if(packet == null) {
			System.err.println("attempting to send but packet is null");
			return;
		}

		try {
			if(sOutput != null)
				sOutput.writeObject(packet);
		} catch (IOException e) {
			System.err.println("error sending packet: ["+packet+", "+packet.getData()+"]");
		}
		
		try {
			if(sOutput != null)
				sOutput.reset();
		} catch (IOException e) {
			System.err.println("error resetting stream");
		}
		
		try {
			if(sOutput != null)
				sOutput.flush();
		} catch (IOException e) {
			System.err.println("error flushing stream");
		}
	}

	/*
	 * Closes the socket output stream.
	 */
	public void close() {
		if(sOutput != null) {
			try {
				sOutput.close();
			} catch (IOException e) {
				System.err.println("error closing stream");
			}
			
			sOutput = null;
		}
	}

}
