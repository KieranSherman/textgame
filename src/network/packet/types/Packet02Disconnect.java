package network.packet.types;

import network.packet.Packet;

/*
 * Class models a disconnect packet
 */
public class Packet02Disconnect extends Packet {
	private static final long serialVersionUID = 1L;
	
	private Packet02Disconnect() {
		super(PacketTypes.DISCONNECT);
	}
	
	public Packet02Disconnect(String data) {
		this();
		super.data = data;
	}

}
