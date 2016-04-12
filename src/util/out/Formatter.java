package util.out;

import network.packet.types.PacketTypes;

/*
 * Class will adjust String before outputting it to text area
 */
public class Formatter {
	
	private Formatter() {}
	
	public static String format(String str, PacketTypes packetType) {
		return getFormat(packetType)+str;
	}
	
	public static String getFormat(PacketTypes packetType) {
		return "[#PACKET"+packetType.getType()+"]";
	}
	
}