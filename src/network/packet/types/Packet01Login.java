package network.packet.types;

import network.packet.Packet;
import network.packet.PacketTypes;

public class Packet01Login extends Packet {

	private String data;
	
	private Packet01Login() {
		super(PacketTypes.LOGIN);
	}
	
	public Packet01Login(String data) {
		this();
		this.data = data;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

}
