package network.packet.types;

import network.packet.Packet;
import util.Resources;

/*
 * Class models a text packet
 */
public class Packet04Action extends Packet {
	private static final long serialVersionUID = 1L;

	public Packet04Action(String data) {
		super(PacketTypes.ACTION, Resources.HOST_ADDRESS);
		super.data = data;
	}

}
