package network;

import network.packet.Packet;
import network.packet.PacketReceiver;
import network.packet.PacketSender;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

/*
 * Class bridges the GUI and the Network
 */
public abstract class Bridge {
	
	protected Logger logger;
	protected PacketSender packetSender;
	protected PacketReceiver packetReceiver;
	
	public Bridge() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		packetInitialization();
	}
	
	public abstract void packetInitialization();
	public abstract void sendPacket(Packet packet);
	public abstract void parsePacket(Packet packet);
	public abstract void writeObject(Object obj);
	public abstract <T> T readObject();
}
