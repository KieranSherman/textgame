package network.packet.types;

import network.packet.Packet;

/*
 * Class models a disconnect packet
 */
public class Packet02Disconnect extends Packet {
	private static final long serialVersionUID = 1L;
	
	public Packet02Disconnect(String data) {
		super(PacketTypes.DISCONNECT);
		super.data = data;
	}

}
