package network.packet;

import network.client.Client;
import network.packet.types.Packet01Login;
import network.server.Server;
import network.util.NetworkTypes;
import util.out.Formatter;
import util.out.Logger;

/*
 * Class handles packets after they've been received
 */
public class PacketParser {
	
	private Server server;
	private Client client;
	
	public PacketParser() {
	
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
		else
		if(networkType == NetworkTypes.SERVER)
			parseClientPacket(packet);
	}
	
	/*
	 * Parse a packet received from the server (i.e client handles this packet)
	 */
	private synchronized void parseServerPacket(Packet packet) {
		switch(packet.getType()) {
			case ACTION: {
				
				break;
			}
			case DISCONNECT: {
				client.disconnect();
				break;
			}
			case LOGIN: {
				
				break;
			}
			case MESSAGE: {
				
				break;
			}
		}

		Logger.appendPacket(packet);
	}
	
	/*
	 * Parse a packet received from the client (i.e server handles this packet)
	 */
	private synchronized void parseClientPacket(Packet packet) {
		Formatter.construct(packet);
		
		switch(packet.getType()) {
			case ACTION: {
				server.sendPacketToAllOtherClients(packet, packet.getHostAddress());
				break;
			}
			case DISCONNECT: {
				
				break;
			}
			case LOGIN: {
				server.registerUser((Packet01Login)packet);
				break;
			}
			case MESSAGE: {
				server.sendPacketToAllOtherClients(packet, packet.getHostAddress());
				break;
			}
		}
		
		Logger.appendPacket(packet);
	}

}
