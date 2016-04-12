package network.packet;

/*
 * Different types of packets
 */
public enum PacketTypes {
	LOGIN(01), DISCONNECT(02), MESSAGE(03), ACTION(04);
	
	private int type;
	
	private PacketTypes(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
