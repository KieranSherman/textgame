package network.packet.types;

import network.packet.Packet;

/**
 * Class models a login packet.
 * 
 * @author kieransherman
 */
public class Packet01Login extends Packet {
	private static final long serialVersionUID = 1L;
	
	private final String username;
	
	/**
	 * Creates a new login packet.
	 * 
	 * @param data the message.
	 * @param username the user's username.
	 */
	public Packet01Login(String data, String username) {
		super(PacketTypes.LOGIN);
		super.data = data;
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
