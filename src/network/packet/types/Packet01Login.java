package network.packet.types;

import network.packet.Packet;

/*
 * Class models a login packet
 */
public class Packet01Login extends Packet {
	private static final long serialVersionUID = 1L;
	
	private Packet01Login() {
		super(PacketTypes.LOGIN);
	}
	
	public Packet01Login(String data) {
		this();
		super.data = data;
	}

}
