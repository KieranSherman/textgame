package network.packet.types;

import network.packet.Packet;
import network.packet.PacketTypes;

/*
 * Class models a text packet
 */
public class Packet03Text extends Packet {
	private static final long serialVersionUID = 1L;

	private String data;
	
	private Packet03Text() {
		super(PacketTypes.TEXT);
	}
	
	public Packet03Text(String data) {
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
