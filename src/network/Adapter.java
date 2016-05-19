package network;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ui.Window;
import main.ui.components.misc.PopupUI;
import main.ui.components.notifications.NotificationPaneUI;
import network.client.Client;
import network.packet.Packet;
import network.packet.PacketParser;
import network.packet.types.Packet02Disconnect;
import network.server.Server;
import network.util.NetworkTypes;
import sound.SoundPlayer;
import util.Action;
import util.Resources;
import util.exceptions.AlreadyRunningNetworkException;

/*
 * Class models a network-UI adapter, bridging the two
 */
public class Adapter {
	
	private boolean block;
	private int blockedPacketCount;
	
	private Server server;		//server object
	private Client client;		//client object
	
	private static PacketParser packetParser;	//packet receiver
	
	private ArrayList<Object[]> blockedPackets;
	
	public Adapter() {
		packetParser = new PacketParser();
		blockedPackets = new ArrayList<Object[]>();
	}

	/*
	 * Create a client connection hostName:portNumber
	 */
	public void createClient(String hostName, int portNumber, String username) {
		try {
			checkNetwork();
			client = new Client(hostName, portNumber);
			client.setUsername(username);

			packetParser.setClient(client);
			startClient();
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Start the client on a thread
	 */
	private void startClient() {
		if(client == null) {
			System.err.println("client not initialized");
			return;
		}
		
		SoundPlayer.play("servoInsert");
		Window.appendColoredText("[client loading...]", Color.ORANGE);
		
		Action action = new Action() {
			private String username;
			
			public void pre() {
				SoundPlayer.play("tapeInsert");
				Window.appendText("[client ready]");
				PopupUI.getInput("USERNAME");
				username = PopupUI.getData();
				client.setUsername(username);
			}
			public void execute() {
				new Thread(client).start();
			}
		};
		
		NotificationPaneUI.queueNotification("CLIENT STARTUP", 1100, action, true);
	}
	
	/*
	 * Destroy the client
	 */
	public void destroyClient() {
		client = null;
		Window.setTitle(Resources.VERSION);
	}
	
	/*
	 * Create a server on 127.0.0.1:portNumber
	 */
	public void createServer(int portNumber) {
		try {
			checkNetwork();
			server = new Server(portNumber);
			
			packetParser.setServer(server);
			startServer();
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Start the server on a thread
	 */
	private void startServer() {
		if(server == null) {
			System.err.println("server not initialized");
			return;
		}
		
		SoundPlayer.play("servoInsert");
		Window.appendColoredText("[server loading...]", Color.ORANGE);
		
		Action action = new Action() {
			public void pre() {
				SoundPlayer.play("tapeInsert");
				Window.appendText("[server ready]");
			}
			public void execute() {
				new Thread(server).start();
			}
		};
		
		NotificationPaneUI.queueNotification("SERVER STARTUP", 1100, action, true);
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
				blockedPackets.add(new Object[] {networkType, packet, ++blockedPacketCount});
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
			
			SoundPlayer.play("servoEject");
			Action action = new Action() {
				public void pre() {
					SoundPlayer.play("tapeInsert");
				}
				public void execute() {
					client.disconnect();
				}
			};
			
			NotificationPaneUI.queueNotification("CLIENT DISCONNECTING", 600, action, true);
		}
		else
		if(server != null) {
			server.sendPacket(new Packet02Disconnect("[server is closing...]"));
			
			SoundPlayer.play("servoEject");
			Action action = new Action() {
				public void pre() {
					SoundPlayer.play("tapeInsert");
				}
				public void execute() {
					server.close();
				}
			};
			
			NotificationPaneUI.queueNotification("SERVER CLOSING", 600, action, true);
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
						Object [] obj = blockedPackets.get(i);
						NetworkTypes networkType = (NetworkTypes) obj[0];
						Packet packet = (Packet) obj[1];
						Window.appendColoredText("["+obj[2]+"/"+blockedPacketCount+"]", Color.DARK_GRAY);
						parsePacket(networkType, packet);
					}
					
					Window.appendColoredText("[...end of blocked packets]", Color.GRAY);
				}
				
				blockedPackets.clear();
				blockedPacketCount = 0;
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
				Window.appendColoredText("[server open at "+InetAddress.getLocalHost().getHostAddress()+"]", Color.CYAN);
			} catch (UnknownHostException e) {
				Window.appendColoredText("[server status unknown]", Color.RED);
			}
		}
		else {
			SoundPlayer.play("error");
			Window.appendColoredText("[no network detected]", Color.RED);
		}
	}
	
	private void checkNetwork() throws AlreadyRunningNetworkException {
		if(server != null || client != null) {
			SoundPlayer.play("error");
			Window.appendColoredText("[network already running]", Color.RED);
			throw new AlreadyRunningNetworkException("You are already running a network!");
		}
	}
	
}
