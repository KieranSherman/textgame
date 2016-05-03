package util.out;

import network.packet.Packet;
import network.packet.types.PacketTypes;

/*
 * Class will adjust String before outputting it to text area
 */
public class Formatter {
	
	private Formatter() {}
	
	public static void construct(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(str+packet.getData());
	}
	
	public static void deconstruct(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(((String)packet.getData()).substring(str.length()));
	}
	
	public static void formatServer(Packet packet) {
		packet.setData("{SERVER} "+packet.getData());
	}
	
	public static void formatUsername(Packet packet, String username) {
		packet.setData("("+username+") "+packet.getData());
	}
	
	private static String getFormat(PacketTypes packetType) {
		return "[PACKET->"+packetType.name()+"] ";
	}
	
}