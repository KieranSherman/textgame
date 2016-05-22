package network.packet;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import network.packet.types.PacketTypes;

/**
 * Class models a packet to be sent and received through input and output streams.
 * 
 * @author kieransherman
 */
public abstract class Packet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String hostAddress;
	private PacketTypes packetType;
	protected Object data;
	
	/**
	 * Creates a new packet.
	 * 
	 * @param packetType the packet type.
	 */
	public Packet(PacketTypes packetType) {
		this.packetType = packetType;
		
		try {
			this.hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the data of the packet.
	 * 
	 * @param data the packet's data as an object.
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * Returns the packet's data.
	 * 
	 * @return the packet's data.
	 */
	public Object getData() { 
		return data;
	}
	
	/**
	 * Returns the packet type of the packet.
	 * 
	 * @return the packet's type
	 */
	public PacketTypes getType() {
		return packetType;
	}
	
	/**
	 * Returns the address the packet originated from.
	 * 
	 * @return the host address.
	 */
	public String getHostAddress() {
		return hostAddress;
	}
}
