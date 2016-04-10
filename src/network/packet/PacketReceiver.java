package network.packet;

import network.NetworkTypes;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class handles packets after they've been received
 */
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
	public synchronized void parsePacket(NetworkTypes networkType, Packet packet) {
		if(networkType == NetworkTypes.CLIENT)
			parseServerPacket(packet);
		
		if(networkType == NetworkTypes.SERVER)
			parseClientPacket(packet);
	}
	
	/*
	 * Parse a packet received from the server
	 */
	private synchronized void parseServerPacket(Packet packet) {
		switch(packet.getType()) {
			case LOGIN: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.LOGIN);
				
				logger.appendText(str);
				break;
			}
			
			case DISCONNECT: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.DISCONNECT);
				
				logger.appendText(str);
				break;
			}
			
			case TEXT: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.TEXT);
				
				logger.appendText(str);
				break;
			}
		}
	}
	
	/*
	 * Parse a packet received from the client
	 */
	private synchronized void parseClientPacket(Packet packet) {
		switch(packet.getType()) {
			case LOGIN: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.LOGIN);
				
				logger.appendText(str);
				break;
			}
			
			case DISCONNECT: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.DISCONNECT);
				
				logger.appendText(str);
				break;
			}
			
			case TEXT: {
				String str = (String) packet.getData();
				str = Formatter.format(str, PacketTypes.TEXT);
				
				logger.appendText(str); 
				break;
			}
		}
	}

}
