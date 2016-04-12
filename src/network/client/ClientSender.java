package network.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import network.packet.Packet;
import network.packet.types.Packet01Login;
import network.packet.types.Packet03Message;

public class ClientSender {
	
	private ObjectOutputStream sOutput;
	
	public ClientSender(Socket socket) throws IOException {
		sOutput = new ObjectOutputStream(socket.getOutputStream());
		sOutput.flush();
		
		init();
	}
	
	private void init() {
		try {
			sendPacket(new Packet01Login("client has connected from "+InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			System.err.println("error sending login packet");
		}
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
	 * closes the output stream
	 */
	protected void close() {
		if(sOutput != null) {
			sendPacket(new Packet03Message("client disconnected"));

			try {
				sOutput.close();
			} catch (IOException e) {
				System.err.println("error closing stream");
			}
			
			sOutput = null;
		}
	}

}
