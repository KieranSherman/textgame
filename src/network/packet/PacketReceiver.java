package network.packet;

import network.NetworkTypes;
import network.client.Client;
import network.server.Server;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

public class PacketReceiver {
	
	private Server server;
	private Client client;
	
	private Logger logger;
	
	private PacketReceiver() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public PacketReceiver(Server server) {
		this();
		this.server = server;
	}
	
	public PacketReceiver(Client client) {
		this();
		this.client = client;
	}
	
	/*
	 * Parse a packet
	 */
	public void parsePacket(Packet packet) {
		if(server == null && client == null)
			return;
		
		if(server == null && client != null)
			parseClientPacket(packet);
		
		if(server != null && client == null)
			parseServerPacket(packet);
			
		if(server != null && client != null)
			parseClientPacket(packet);
	}
	
	/*
	 * Parse a packet 
	 */
	public void parsePacket(NetworkTypes networkType, Packet packet) {
		if(networkType == NetworkTypes.SERVER)
			parseServerPacket(packet);
		
		if(networkType == NetworkTypes.CLIENT)
			parseClientPacket(packet);
	}
	
	/*
	 * Parse a packet received from the server
	 */
	private void parseServerPacket(Packet packet) {
		switch(packet.getType()) {
			case LOGIN: {
				logger.appendText("you have connected.");
			}
			
			case DISCONNECT: {
				logger.appendText("you have disconnected.");
			}
		}
	}
	
	/*
	 * Parse a packet received from the client
	 */
	private void parseClientPacket(Packet packet) {
		switch(packet.getType()) {
			case LOGIN: {
				logger.appendText(packet.getData()+" has connected.");
			}
			
			case DISCONNECT: {
				logger.appendText(packet.getData()+" has disconnected.");
			}
		}
	}

}
