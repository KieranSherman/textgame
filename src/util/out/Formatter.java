package util.out;

import network.packet.Packet;
import network.packet.types.PacketTypes;

/**
 * Class consists exclusively of static methods which format a packet.
 * 
 * @author kieransherman
 *
 */
public class Formatter {
	
	// Prevent object instantiation
	private Formatter() {}

	/**
	 * Constructs a packet.
	 * 
	 * @param packet the packet to construct.
	 */
	public static void construct(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(str+packet.getData());
	}
	
	/**
	 * Deconstructs a packet.
	 * 
	 * @param packet the packet to deconstruct.
	 */
	public static void deconstruct(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(((String)packet.getData()).substring(str.length()));
	}
	
	/**
	 * Formats a packet from the server.
	 * 
	 * @param packet the packet to format.
	 */
	public static void formatServerPacket(Packet packet) {
		packet.setData("{SERVER} "+packet.getData());
	}
	
	/**
	 * Formats a packet to include the username.
	 * 
	 * @param packet the packet to format.
	 * @param username the username to include.
	 */
	public static void formatUsername(Packet packet, String username) {
		packet.setData("("+username+") "+packet.getData());
	}
	
	/**
	 * Return the format of packetType. 
	 */
	private static String getFormat(PacketTypes packetType) {
		return "[PACKET-->"+packetType.name()+"] ";
	}
	
}