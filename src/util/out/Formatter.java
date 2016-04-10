package util.out;

import network.packet.PacketTypes;

/*
 * Class will adjust String before outputting it to text area
 */
public class Formatter {
	
	public static String format(String str, PacketTypes packetType) {
		return getFormat(packetType)+str;
	}
	
	public static String getFormat(PacketTypes packetType) {
		switch(packetType) {
			case LOGIN: {
				return "**LOGIN**";
			}
				
			case DISCONNECT: {
				return "**DISCONNECT**";
			}
				
			case TEXT: {
				return "**TEXT**";
			}
		}
		
		return null;
	}
	
}