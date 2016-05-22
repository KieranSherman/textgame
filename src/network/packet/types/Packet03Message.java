package network.packet.types;

import network.packet.Packet;

/**
 * Class models a message packet.
 * 
 * @author kieransherman
 */
public class Packet03Message extends Packet {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new message packet.
	 * 
	 * @param data the message.
	 */
	public Packet03Message(String data) {
		super(PacketTypes.MESSAGE);
		super.data = data;
	}

}
