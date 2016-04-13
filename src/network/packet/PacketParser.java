package network.packet;

import network.NetworkTypes;
import network.client.Client;
import network.packet.types.PacketTypes;
import network.server.Server;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class handles packets after they've been received
 */
public class PacketParser {
	
	private Logger logger;
	@SuppressWarnings("unused")
	private Server server;
	private Client client;
	
	public PacketParser() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setClient(Client client) {
		this.client = client;
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
	 * Parse a packet received from the server (i.e client handles this packet)
	 */
	private synchronized void parseServerPacket(Packet packet) {
		packet = Formatter.format(packet);

		if(packet.getType() == PacketTypes.DISCONNECT)
			client.disconnect();
		
		logger.appendPacket(packet);

	}
	
	/*
	 * Parse a packet received from the client (i.e server handles this packet)
	 */
	private synchronized void parseClientPacket(Packet packet) {
		packet = Formatter.format(packet);
		logger.appendPacket(packet);
	}

}
