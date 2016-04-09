package network.packet;

import network.NetworkTypes;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

public class PacketReceiver {
	
	private Logger logger;
	
	public PacketReceiver() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
			
			case TEXT: {
				logger.appendText((String) packet.getData());
			}
		}
	}
	
	/*
	 * Parse a packet received from the client
	 */
	private void parseClientPacket(Packet packet) {
		switch(packet.getType()) {
			case LOGIN: {
				logger.appendText((String) packet.getData()+" has connected.");
			}
			
			case DISCONNECT: {
				logger.appendText((String) packet.getData()+" has disconnected.");
			}
			
			case TEXT: {
				logger.appendText((String) packet.getData()); 
			}
		}
	}

}
