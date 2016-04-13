package network.packet.types;

import network.packet.Packet;

/*
 * Class models a text packet
 */
public class Packet03Message extends Packet {
	private static final long serialVersionUID = 1L;

	private Packet03Message() {
		super(PacketTypes.MESSAGE);
	}
	
	public Packet03Message(String data) {
		this();
		super.data = data;
	}

}
