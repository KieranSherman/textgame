package network.packet;

public abstract class Packet {
	
	private PacketTypes packetType;
	
	public Packet(PacketTypes packetType) {
		this.packetType = packetType;
	}
	
	public abstract <T> T getData(); 
	
	public PacketTypes getType() {
		return packetType;
	}
}
