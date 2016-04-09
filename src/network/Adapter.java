package network;

import network.client.Client;
import network.client.ClientBridge;
import network.packet.Packet;
import network.server.Server;
import network.server.ServerBridge;

public class Adapter {

	private static Server server;
	private static Client client;
	
	private static ServerBridge server_bridge;
	private static ClientBridge client_bridge;
	
	public void createClient() {
		client = new Client();
		bridge(NetworkTypes.CLIENT);
	}
	
	public void createClient(String hostName) {
		client = new Client(hostName);
		bridge(NetworkTypes.CLIENT);
	}
	
	public void createClient(String hostName, int portNumber) {
		client = new Client(hostName, portNumber);
		bridge(NetworkTypes.CLIENT);
	}
	
	public void startClient() {
		System.out.println("running client");
		new Thread(client).start();
	}
	
	public void createServer() {
		server = new Server();
		bridge(NetworkTypes.SERVER);
	}
	
	public void createServer(int portNumber) {
		server = new Server(portNumber);
		bridge(NetworkTypes.SERVER);
	}
	
	public void startServer() {
		System.out.println("running server");
		new Thread(server).start();
	}
	
	public void sendPacket(Packet packet) {
		if(server == null && client == null)
			return;
		
		if(server == null && client != null)
			client_bridge.sendPacket(packet);
		
		if(server != null && client == null)
			server_bridge.sendPacket(packet);
		
		if(server != null && client != null)
			client_bridge.sendPacket(packet);
	}
	
	public void sendPacket(NetworkTypes networkType, Packet packet) {
		if(networkType == NetworkTypes.CLIENT && client != null)
			client_bridge.sendPacket(packet);
		
		if(networkType == NetworkTypes.SERVER && server != null)
			server_bridge.sendPacket(packet);
	}
	
	public void parsePacket(NetworkTypes networkTypes, Packet packet) {
		if(networkTypes == NetworkTypes.CLIENT)
			client_bridge.parsePacket(packet);
		
		if(networkTypes == NetworkTypes.SERVER)
			server_bridge.parsePacket(packet);
	}
	
	public void writeObject(Object obj) {
		if(server == null && client == null)
			return;
		
		if(server == null && client != null)
			client_bridge.writeObject(obj);
		
		if(server != null && client == null)
			server_bridge.writeObject(obj);
		
		if(server != null && client != null)
			client_bridge.writeObject(obj);
	}
	
	public void writeObject(NetworkTypes networkType, Object obj) {
		if(networkType == NetworkTypes.CLIENT && client != null)
			client_bridge.writeObject(obj);
		
		if(networkType == NetworkTypes.SERVER && server != null)
			server_bridge.writeObject(obj);
	}
	
	public void close() {
		if(client != null) {
			client.disconnect();
			client = null;
			client_bridge = null;
		}
		
		if(server != null) {
			server.close();
			server = null;
			server_bridge = null;
		}
	}
	
	private void bridge(NetworkTypes networkType) {
		if(networkType == NetworkTypes.CLIENT)
			client_bridge = new ClientBridge(client);
		
		if(networkType == NetworkTypes.SERVER)
			server_bridge = new ServerBridge(server);
	}
	
}
