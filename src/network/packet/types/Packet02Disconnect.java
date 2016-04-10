package network.packet.types;

import network.packet.Packet;
import network.packet.PacketTypes;

/*
 * Class models a disconnect packet
 */
public class Packet02Disconnect extends Packet {
	private static final long serialVersionUID = 1L;
	
	private String data;
	
	private Packet02Disconnect() {
		super(PacketTypes.LOGIN);
	}
	
	public Packet02Disconnect(String data) {
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