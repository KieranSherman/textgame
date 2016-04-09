package network.client;

import network.Bridge;
import network.NetworkTypes;
import network.packet.Packet;
import network.packet.PacketReceiver;
import network.packet.PacketSender;

public class ClientBridge extends Bridge {
	
	private Client client;
	
	public ClientBridge(Client client) {
		super();
		this.client = client;
	}
	
	@Override
	public void packetInitialization() {
		super.packetSender = new PacketSender(client);
		super.packetReceiver = new PacketReceiver(client);
	}

	@Override
	public void sendPacket(Packet packet) {
		super.packetSender.sendPacket(packet);
	}
	
	@Override
	public void parsePacket(Packet packet) {
		super.packetReceiver.parsePacket(NetworkTypes.SERVER, packet);
	}
	
	@Override
	public void writeObject(Object obj) {
		client.writeObject(obj);
	}

	@Override
	public <T> T readObject() {
		Object obj = client.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return client.readObject();
	}




}