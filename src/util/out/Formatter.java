package util.out;

import network.packet.Packet;
import network.packet.types.PacketTypes;

/*
 * Class will adjust String before outputting it to text area
 */
public class Formatter {
	
	private Formatter() {}
	
	public static Packet format(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(str+packet.getData());
		
		return packet;
	}
	
	public static Packet unformat(Packet packet) {
		String str = getFormat(packet.getType());
		packet.setData(((String)packet.getData()).substring(str.length()));
		
		return packet;
	}
	
	public static Packet formatUsername(Packet packet, String username) {
		packet.setData("("+username+")"+packet.getData());
		
		return packet;
	}
	
	public static String getFormat(PacketTypes packetType) {
		return "[PACKET->"+packetType.name()+"]";
	}
	
}