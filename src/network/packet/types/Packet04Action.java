package network.packet.types;

import network.packet.Packet;

/**
 * Class models an action packet.
 * 
 * @author kieransherman
 */
public class Packet04Action extends Packet {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new action packet.
	 * 
	 * @param data the action.
	 */
	public Packet04Action(String data) {
		super(PacketTypes.ACTION);
		super.data = data;
	}

}
