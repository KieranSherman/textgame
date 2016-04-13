package network.packet.types;

import network.packet.Packet;

/*
 * Class models a text packet
 */
public class Packet04Action extends Packet {
	private static final long serialVersionUID = 1L;

	private Packet04Action() {
		super(PacketTypes.ACTION);
	}
	
	public Packet04Action(String data) {
		this();
		super.data = data;
	}

}
