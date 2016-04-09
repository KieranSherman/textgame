package network.server;

import network.Bridge;
import network.NetworkTypes;
import network.packet.Packet;
import network.packet.PacketReceiver;
import network.packet.PacketSender;

public class ServerBridge extends Bridge {
	
	private Server server;
	
	public ServerBridge(Server server) {
		super();
		this.server = server;
	}
	
	@Override
	public void packetInitialization() {
		super.packetSender = new PacketSender(server);
		super.packetReceiver = new PacketReceiver(server);
	}
	
	@Override
	public void sendPacket(Packet packet) {
		super.packetSender.sendPacket(packet);
	}
	
	@Override
	public void parsePacket(Packet packet) {
		super.packetReceiver.parsePacket(NetworkTypes.CLIENT, packet);
	}
	
	@Override
	public void writeObject(Object obj) {
		server.writeObject(obj);
	}

	@Override
	public <T> T readObject() {
		Object obj = server.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return server.readObject();
	}

}
