package network.server.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.upnp.UPnPGateway;

/**
 * Class sends packets over a socket.
 * 
 * @author kieransherman
 * 
 */
public class ServerSender {
	
	private ObjectOutputStream sOutput;
	
	/**
	 * Creates a new object to send packets with.
	 * 
	 * @param socket the socket to send over.
	 * @throws IOException the socket is closed.
	 */
	public ServerSender(Socket socket) throws IOException {
		sOutput = new ObjectOutputStream(socket.getOutputStream());
		sOutput.flush();
		
		init();
	}
	
	/**
	 * Send a login packet to the client.
	 */
	private void init() {
		sendPacket(new Packet01Login("[you have connected to "+UPnPGateway.getMappedAddress()+"]", null));
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
			System.err.println("error resetting output stream");
		}
		
		try {
			if(sOutput != null)
				sOutput.flush();
		} catch (IOException e) {
			System.err.println("error flushing output stream");
		}
	}
	
	/**
	 * Closes the output stream.
	 */
	protected void close() {
		if(sOutput != null) {
			try {
				sOutput.close();
			} catch (IOException e) {
				System.err.println("error closing output stream");
			}
			
			sOutput = null;
		}
	}

}
