package network.packet;

import network.NetworkTypes;
import network.client.Client;
import network.server.Server;

public class PacketSender {
	
	private Server server;
	private Client client;
	
	public PacketSender(Server server) {
		this.server = server;
	}
	
	public PacketSender(Client client) {
		this.client = client;
	}
	
	public void sendPacket(Packet packet) {
		if(server == null && client == null)
			return;
		
		if(server == null && client != null)
			client.writeObject(packet.getData());
		
		if(server != null && client == null)
			server.writeObject(packet.getData());
			
		if(server != null && client != null)
			client.writeObject(packet.getData());
	}
	
	public void sendPacket(NetworkTypes networkType, Packet packet) {
		if(networkType == NetworkTypes.SERVER)
			server.writeObject(packet.getData());
			
		if(networkType == NetworkTypes.CLIENT)
			client.writeObject(packet.getData());
	}
	
}
