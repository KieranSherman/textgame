package network.packet.types;

import network.packet.Packet;

/*
 * Class models a text packet
 */
public class Packet04Action extends Packet {
	private static final long serialVersionUID = 1L;

	public Packet04Action(String data) {
		super(PacketTypes.ACTION);
		super.data = data;
	}

}
