package network.packet.types;

import network.packet.Packet;

/*
 * Class models a text packet
 */
public class Packet03Message extends Packet {
	private static final long serialVersionUID = 1L;

	private String data;
	
	private Packet03Message() {
		super(PacketTypes.MESSAGE);
	}
	
	public Packet03Message(String data) {
		this();
		this.data = data;
	}

	@Override
	@SuppressWarnings("unchecked")
	/*
	 * returns the packet's data
	 */
	public <T> T getData() {
		return (T) data;
	}
}
