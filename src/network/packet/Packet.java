package network.packet;

import java.io.Serializable;

public abstract class Packet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PacketTypes packetType;
	
	public Packet(PacketTypes packetType) {
		this.packetType = packetType;
	}
	
	public abstract <T> T getData(); 
	
	public PacketTypes getType() {
		return packetType;
	}
}
