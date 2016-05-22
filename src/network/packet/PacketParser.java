package network.packet;

import network.client.Client;
import network.packet.types.Packet01Login;
import network.server.Server;
import network.util.NetworkTypes;
import util.out.Formatter;
import util.out.DefaultLogger;

/*
 * Class handles packets after they've been received
 */
public class PacketParser {
	
	private PacketParser() {}
	
	/*
	 * Parse a packet 
	 */
	public static synchronized void parsePacket(NetworkTypes networkType, Packet packet) {
		if(networkType == NetworkTypes.CLIENT)
			parseServerPacket(packet);
		else
		if(networkType == NetworkTypes.SERVER)
			parseClientPacket(packet);
	}
	
	/*
	 * Parse a packet received from the server (i.e client handles this packet)
	 */
	private static synchronized void parseServerPacket(Packet packet) {
		switch(packet.getType()) {
			case ACTION: {
				
				break;
			}
			case DISCONNECT: {
				Client.disconnect();
				break;
			}
			case LOGIN: {
				
				break;
			}
			case MESSAGE: {
				
				break;
			}
		}

		DefaultLogger.appendPacket(packet);
	}
	
	/*
	 * Parse a packet received from the client (i.e server handles this packet)
	 */
	private static synchronized void parseClientPacket(Packet packet) {
		Formatter.construct(packet);
		
		switch(packet.getType()) {
			case ACTION: {
				Server.sendPacketToAllOtherClients(packet, packet.getHostAddress());
				break;
			}
			case DISCONNECT: {
				
				break;
			}
			case LOGIN: {
				Server.registerUser((Packet01Login)packet);
				break;
			}
			case MESSAGE: {
				Server.sendPacketToAllOtherClients(packet, packet.getHostAddress());
				break;
			}
		}
		
		DefaultLogger.appendPacket(packet);
	}

}
