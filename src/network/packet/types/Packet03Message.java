package network.packet.types;

import network.packet.Packet;

/*
 * Class models a text packet
 */
public class Packet03Message extends Packet {
	private static final long serialVersionUID = 1L;

	public Packet03Message(String data) {
		super(PacketTypes.MESSAGE);
		super.data = data;
	}

}
