package network.server.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.upnp.UPnPGateway;

public class ServerSender {
	
	private ObjectOutputStream sOutput;
	
	public ServerSender(Socket socket) throws IOException {
		sOutput = new ObjectOutputStream(socket.getOutputStream());
		sOutput.flush();
		
		init();
	}
	
	private void init() {
		sendPacket(new Packet01Login("[you have connected to "+UPnPGateway.getMappedAddress()+"]", null));
	}
	
	/*
	 * sends a packet over the output stream
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
	
	/*
	 * closes the output stream
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
