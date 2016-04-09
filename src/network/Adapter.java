package network;

import network.client.Client;
import network.client.ClientBridge;
import network.server.Server;
import network.server.ServerBridge;

public class Adapter {

	private static Server server;
	private static Client client;
	
	private static ServerBridge server_bridge;
	private static ClientBridge client_bridge;
	
	public void createClient() {
		client = new Client();
		bridge(NetworkType.CLIENT);
	}
	
	public void createClient(String hostName) {
		client = new Client(hostName);
		bridge(NetworkType.CLIENT);
	}
	
	public void createClient(String hostName, int portNumber) {
		client = new Client(hostName, portNumber);
		bridge(NetworkType.CLIENT);
	}
	
	public void startClient() {
		new Thread(client).start();
		System.out.println("running client");
	}
	
	public void createServer() {
		server = new Server();
		bridge(NetworkType.SERVER);
	}
	
	public void createServer(int portNumber) {
		server = new Server(portNumber);
		bridge(NetworkType.SERVER);
	}
	
	public void startServer() {
		new Thread(server).start();
		System.out.println("running server");
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
	
	public void writeObject(NetworkType networkType, Object obj) {
		if(networkType == NetworkType.CLIENT)
			client_bridge.writeObject(obj);
		
		if(networkType == NetworkType.SERVER)
			server_bridge.writeObject(obj);
	}
	
	public void close() {
		if(client != null)
			client.disconnect();
		
		if(server != null)
			server.close();
	}
	
	private void bridge(NetworkType networkType) {
		if(networkType == NetworkType.CLIENT)
			client_bridge = new ClientBridge(client);
		
		if(networkType == NetworkType.SERVER)
			server_bridge = new ServerBridge(server);
	}
	
}
