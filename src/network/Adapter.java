package network;

import network.client.Client;
import network.packet.Packet;
import network.packet.PacketReceiver;
import network.server.Server;

/*
 * Class models a network-UI adapter, bridging the two
 */
public class Adapter  {
	
	private Server server;		//server object
	private Client client;		//client object
	
	private static PacketReceiver packetReceiver;	//packet receiver
	
	public Adapter() {
		packetReceiver = new PacketReceiver();
	}
	
	/*
	 * Create a client
	 */
	public void createClient() {
		client = new Client();
	}
	
	/*
	 * Create a client connecting to hostName
	 */
	public void createClient(String hostName) {
		client = new Client(hostName);
	}
	
	/*
	 * Create a client connection hostName:portNumber
	 */
	public void createClient(String hostName, int portNumber) {
		client = new Client(hostName, portNumber);
	}
	
	/*
	 * Start the client on a thread
	 */
	public void startClient() {
		System.out.println("running client");
		new Thread(client).start();
	}
	
	/*
	 * Create a server
	 */
	public void createServer() {
		server = new Server();
	}
	
	/*
	 * Create a server on 127.0.0.1:portNumber
	 */
	public void createServer(int portNumber) {
		server = new Server(portNumber);
	}
	
	/*
	 * Start the server on a thread
	 */
	public void startServer() {
		System.out.println("running server");
		new Thread(server).start();
	}
	
	/*
	 * Send a packet
	 */
	public synchronized void sendPacket(Packet packet) {
		if(packet == null) {
			System.err.println("error constructing packet");
			return;
		}
		
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
	
	/*
	 * Parse a packet
	 */
	public synchronized void parsePacket(NetworkTypes networkTypes, Packet packet) {
		if(networkTypes == NetworkTypes.CLIENT && client != null)
			packetReceiver.parsePacket(NetworkTypes.CLIENT, packet);
		
		if(networkTypes == NetworkTypes.SERVER && server != null)
			packetReceiver.parsePacket(NetworkTypes.SERVER, packet);
	}

	/*
	 * Close down connections
	 */
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
