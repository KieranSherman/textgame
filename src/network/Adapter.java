package network;

import network.client.Client;
import network.packet.Packet;
import network.packet.PacketReceiver;
import network.server.Server;

public class Adapter  {

	private static Server server;
	private static Client client;
	
	private static PacketReceiver packetReceiver;
	
	public Adapter() {
		packetReceiver = new PacketReceiver();
	}
	
	public void createClient() {
		client = new Client();
	}
	
	public void createClient(String hostName) {
		client = new Client(hostName);
	}
	
	public void createClient(String hostName, int portNumber) {
		client = new Client(hostName, portNumber);
	}
	
	public void startClient() {
		System.out.println("running client");
		new Thread(client).start();
	}
	
	public void createServer() {
		server = new Server();
	}
	
	public void createServer(int portNumber) {
		server = new Server(portNumber);
	}
	
	public void startServer() {
		System.out.println("running server");
		new Thread(server).start();
	}
	
	public void sendPacket(Packet packet) {		
		if(server == null && client == null)
			return;
		
		//If the user is running a client
		if(server == null && client != null)
			client.sendPacket(packet);
		
		//If the user is running a server
		if(server != null && client == null)
			server.sendPacket(packet);
		
		//If the user is running a server and a client
		if(server != null && client != null)
			client.sendPacket(packet);
	}
	
	public void parsePacket(NetworkTypes networkTypes, Packet packet) {
		if(networkTypes == NetworkTypes.CLIENT && client != null)
			packetReceiver.parsePacket(NetworkTypes.CLIENT, packet);
		
		if(networkTypes == NetworkTypes.SERVER && server != null)
			packetReceiver.parsePacket(NetworkTypes.SERVER, packet);
	}

	public void close() {
		if(client != null) {
			client.disconnect();
			client = null;
		}
		
		if(server != null) {
			server.close();
			server = null;
		}
	}
	
}
