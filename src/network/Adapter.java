package network;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ui.Window;
import network.client.Client;
import network.packet.Packet;
import network.packet.PacketParser;
import network.packet.types.Packet02Disconnect;
import network.server.Server;
import util.Resources;
import util.exceptions.AlreadyRunningNetworkException;

/*
 * Class models a network-UI adapter, bridging the two
 */
public class Adapter  {
	
	private boolean block;
	
	private Server server;		//server object
	private Client client;		//client object
	
	private static PacketParser packetParser;	//packet receiver
	
	private ArrayList<Object[]> blockedPackets;
	
	public Adapter() {
		packetParser = new PacketParser();
		blockedPackets = new ArrayList<Object[]>();
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
		Window.setTitle(Resources.VERSION);
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
		Window.setTitle(Resources.VERSION);
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
		else
		if(client != null)
			client.sendPacket(packet);
	}
	
	/*
	 * Parse a packet
	 */
	public synchronized void parsePacket(NetworkTypes networkType, Packet packet) {
		if(block) {
			synchronized(blockedPackets) {
				blockedPackets.add(new Object[] {networkType, packet});
			}
			return;
		}
		
		if(networkType == NetworkTypes.CLIENT && client != null)
			packetParser.parsePacket(NetworkTypes.CLIENT, packet);
		
		if(networkType == NetworkTypes.SERVER && server != null)
			packetParser.parsePacket(NetworkTypes.SERVER, packet);
	}

	/*
	 * Close down connections
	 */
	public void close() {
		if(client != null) {
			client.sendPacket(new Packet02Disconnect("[client is disconnecting...]"));
			client.disconnect();
		}
		else
		if(server != null) {
			server.sendPacket(new Packet02Disconnect("[server is closing...]"));
			server.close();
		}
	}
	
	/*
	 * Blocks incoming connections
	 */
	public void block(boolean showBlockedPackets) {
		block = !block;

		if(block == false) {
			Window.appendColoredText("[removed block from incoming connections]", Color.GRAY);
			
			synchronized(blockedPackets) {
				if(showBlockedPackets) {
					Window.appendColoredText("[showing blocked packets...]", Color.GRAY);
	
					for(int i = 0; i < blockedPackets.size(); i++) {
						Object [] obj = blockedPackets.remove(i);
						NetworkTypes networkType = (NetworkTypes) obj[0];
						Packet packet = (Packet) obj[1];
						parsePacket(networkType, packet);
					}
					
					Window.appendColoredText("[...end of blocked packets]", Color.GRAY);
				}
				
				blockedPackets.clear();
			}
		} else
		if(block == true) {
			Window.appendColoredText("[blocking incoming connections]", Color.GRAY);
		}
	}
	
	/*
	 * Displays the status of the network
	 */
	public void status() {
		if(client != null) {
			Window.appendColoredText("[client connected to server]", Color.CYAN);
		}
		else
		if(server != null) {
			try {
				Window.appendColoredText("[server open at "+
						InetAddress.getLocalHost().getHostAddress()+"]", Color.CYAN);
			} catch (UnknownHostException e) {
				Window.appendColoredText("[server status unkown]", Color.RED);
			}
		}
		else {
			Window.appendColoredText("[no network detected]", Color.RED);
		}
	}
	
	private void checkNetwork() throws AlreadyRunningNetworkException {
		if(server != null || client != null)
			throw new AlreadyRunningNetworkException("You are already running a network!");
	}
	
}
