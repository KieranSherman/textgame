package network;

import network.bridge.ClientBridge;
import network.bridge.ServerBridge;
import network.bridge.Bridge.BridgeType;

public class Adapter {

	private Server server;
	private Client client;
	
	@SuppressWarnings("unused")
	private ServerBridge server_bridge;
	@SuppressWarnings("unused")
	private ClientBridge client_bridge;
	
	public void createClient() {
		client = new Client();
		bridge(BridgeType.CLIENT);
	}
	
	public void createClient(String hostName) {
		client = new Client(hostName);
		bridge(BridgeType.CLIENT);
	}
	
	public void createClient(String hostName, int portNumber) {
		client = new Client(hostName, portNumber);
		bridge(BridgeType.CLIENT);
	}
	
	public void createServer() {
		server = new Server();
		bridge(BridgeType.SERVER);
	}
	
	public void createServer(int portNumber) {
		server = new Server(portNumber);
		bridge(BridgeType.SERVER);
	}
	
	private void bridge(BridgeType bridgeType) {
		if(bridgeType == BridgeType.CLIENT)
			client_bridge = new ClientBridge(client);
		
		if(bridgeType == BridgeType.SERVER)
			server_bridge = new ServerBridge(server);
	}
	
}
