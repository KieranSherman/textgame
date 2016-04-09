package network.packet.types;

import network.packet.Packet;
import network.packet.PacketTypes;

public class Packet03Text extends Packet {
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
	public <T> T getData() {
		return (T) data;
	}
}
