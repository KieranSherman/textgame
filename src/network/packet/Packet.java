package network.packet;

import java.io.Serializable;

import network.packet.types.PacketTypes;

/*
 * Class models a packet to be sent and received through 
 * input/output streams
 */
public abstract class Packet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PacketTypes packetType;		//what kind of packet it is
	protected Object data;
	
	public Packet(PacketTypes packetType) {
		this.packetType = packetType;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() { 	//return the packet's data
		return data;
	}
	
	public PacketTypes getType() {		//return the packet's type
		return packetType;
	}
}
