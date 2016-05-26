package network.packet.types;

/**
 * Enum models different types of packets.
 * 
 * @author kieransherman
 * 
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
