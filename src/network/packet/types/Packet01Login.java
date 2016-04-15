package network.packet.types;

import network.packet.Packet;
import util.Resources;

/*
 * Class models a login packet
 */
public class Packet01Login extends Packet {
	private static final long serialVersionUID = 1L;
	
	public Packet01Login(String data) {
		super(PacketTypes.LOGIN, Resources.HOST_ADDRESS);
		super.data = data;
	}

}
