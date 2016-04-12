package network;

import network.client.Client;
import network.packet.Packet;
import network.packet.PacketParser;
import network.packet.types.Packet02Disconnect;
import network.server.Server;
import util.exceptions.AlreadyRunningNetworkException;

/*
 * Class models a network-UI adapter, bridging the two
 */
public class Adapter  {
	
	private Server server;		//server object
	private Client client;		//client object
	
	private static PacketParser packetParser;	//packet receiver
	
	public Adapter() {
		packetParser = new PacketParser();
	}
	
	/*
	 * Create a client
	 */
	public void createClient() {
		try {
			checkNetwork();
			client = new Client();
			packetParser.setClient(client);
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create a client connecting to hostName
	 */
	public void createClient(String hostName) {
		try {
			checkNetwork();
			client = new Client(hostName);
			packetParser.setClient(client);
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create a client connection hostName:portNumber
	 */
	public void createClient(String hostName, int portNumber) {
		try {
			checkNetwork();
			client = new Client(hostName, portNumber);
			packetParser.setClient(client);
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Start the client on a thread
	 */
	public void startClient() {
		if(client == null) {
			System.err.println("client not initialized");
			return;
		}
		
		System.out.println("running client");
		new Thread(client).start();
	}
	
	/*
	 * Destroy the client
	 */
	public void destroyClient() {
		client = null;
	}
	
	/*
	 * Create a server
	 */
	public void createServer() {
		try {
			checkNetwork();
			server = new Server();
			packetParser.setServer(server);
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create a server on 127.0.0.1:portNumber
	 */
	public void createServer(int portNumber) {
		try {
			checkNetwork();
			server = new Server(portNumber);
			packetParser.setServer(server);
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Start the server on a thread
	 */
	public void startServer() {
		if(server == null) {
			System.err.println("server not initialized");
			return;
		}
		
		System.out.println("running server");
		new Thread(server).start();
	}
	
	/*
	 * Destory the server
	 */
	public void destroyServer() {
		server = null;
	}
	
	/*
	 * Send a packet
	 */
	public synchronized void sendPacket(Packet packet) {
		if(packet == null) {
			System.err.println("error constructing packet");
			return;
		}
		
		if(server != null)
			server.sendPacket(packet);
		
		if(client != null)
			client.sendPacket(packet);
	}
	
	/*
	 * Parse a packet
	 */
	public synchronized void parsePacket(NetworkTypes networkTypes, Packet packet) {
		if(networkTypes == NetworkTypes.CLIENT && client != null)
			packetParser.parsePacket(NetworkTypes.CLIENT, packet);
		
		if(networkTypes == NetworkTypes.SERVER && server != null)
			packetParser.parsePacket(NetworkTypes.SERVER, packet);
	}

	/*
	 * Close down connections
	 */
	public void close() {
		if(client != null) {
			client.sendPacket(new Packet02Disconnect("client is disconnecting..."));
			client.disconnect();
		}
		
		if(server != null) {
			server.sendPacket(new Packet02Disconnect("server is closing..."));
			server.close();
		}
	}
	
	private void checkNetwork() throws AlreadyRunningNetworkException {
		if(server != null || client != null)
			throw new AlreadyRunningNetworkException("You are already running a network!");
	}
	
}
