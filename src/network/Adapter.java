package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ui.Window;
import main.ui.components.display.notification.NotificationUI;
import main.ui.components.display.status.StatusUI;
import main.ui.components.popup.PopupUI;
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
import util.out.DefaultLogger;

/**
 * Class models a network <-> UI adapter, bridging the two.
 * 
 * @author kieransherman
 */
public class Adapter {
	
	private static boolean block;
	private static int blockedPacketCount;
	
	private static ArrayList<Object[]> blockedPackets;
	
	// Prevent object instantiation
	private Adapter() {}
	
	static {
		blockedPackets = new ArrayList<Object[]>();
	}

	/**
	 * Creates and connects a client to [hostAddress:port].
	 * 
	 * @param hostAddress the address to connect to. 
	 * @param portNumber the port number to connect to.
	 */
	public static void createClient(String hostAddress, int portNumber) {
		try {
			Adapter.checkNetwork();
			Client.initialize(hostAddress, portNumber);
			Adapter.startClient();
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start the client on a thread.
	 */
	private static void startClient() {
		if(!Client.isInitialized()) {
			System.err.println("client not initialized");
			return;
		}
		
		SoundPlayer.play("servoInsert");
		DefaultLogger.appendColoredText("[client loading...]", Color.ORANGE);
		
		Action clientStartup = new Action() {
			private String username;
			
			public void pre() {
				SoundPlayer.play("tapeInsert");
				DefaultLogger.appendText("[client ready]");
				PopupUI.promptInput("USERNAME", false);
				
				if(PopupUI.getData() == null)
					username = "anonymous";
				else
					username = (String)PopupUI.getData()[0];
				Client.setUsername(username);
			}
			public void execute() {
				Client.startClient();
			}
		};
		
		NotificationUI.queueNotification("CLIENT STARTUP", 1100, clientStartup, true);
	}
	
	
	/**
	 * Destroy the client.
	 */
	public static void destroyClient() {
		Client.disconnect();
		Window.getFrame().setTitle(Resources.CURRENT_VERSION);
	}
	
	
	/**
	 * Create a server at a port.
	 * 
	 * @param portNumber the port number.
	 */
	public static void createServer(int portNumber) {
		try {
			Adapter.checkNetwork();
			Server.initialize(portNumber);
			Adapter.startServer();
		} catch (AlreadyRunningNetworkException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start the server.
	 */
	private static void startServer() {
		if(!Server.isInitialized()) {
			System.err.println("server not initialized");
			return;
		}
		
		SoundPlayer.play("servoInsert");
		DefaultLogger.appendColoredText("[server loading...]", Color.ORANGE);
		
		Action serverStartup = new Action() {
			public void pre() {
				SoundPlayer.play("tapeInsert");
				DefaultLogger.appendText("[server ready]");
			}
			public void execute() {
				Server.startServer();
			}
			public void post() {
				StatusUI.createStatusDisplay(NotificationUI.getPanel(), BorderLayout.SOUTH);
			}
		};
		
		NotificationUI.queueNotification("SERVER STARTUP", 1100, serverStartup, true);
	}
	
	/**
	 * Destroy the server
	 */
	public static void destroyServer() {
		Server.close();
		Window.getFrame().setTitle(Resources.CURRENT_VERSION);
		StatusUI.removeStatusDisplay(NotificationUI.getPanel());
	}
	
	/**
	 * Send a packet. Automatically determines over which network to send it.
	 * 
	 * @param packet the packet to send.
	 */
	public static synchronized void sendPacket(Packet packet) {
		if(packet == null) {
			System.err.println("error constructing packet");
			return;
		}
		
		if(Server.isRunning())
			Server.sendPacketToAllClients(packet);
		else
		if(Client.isRunning())
			Client.sendPacket(packet);
	}
	
	
	/**
	 * Parse a packet based on network type.
	 * 
	 * @param networkType the destination.
	 * @param packet the pazcket to parse.
	 */
	public static synchronized void parsePacket(NetworkTypes networkType, Packet packet) {
		if(block) {
			synchronized(blockedPackets) {
				blockedPackets.add(new Object[] {networkType, packet, ++blockedPacketCount});
			}
			return;
		}
		
		if(networkType == NetworkTypes.CLIENT && Client.isRunning())
			PacketParser.parsePacket(NetworkTypes.CLIENT, packet);
		
		if(networkType == NetworkTypes.SERVER && Server.isRunning())
			PacketParser.parsePacket(NetworkTypes.SERVER, packet);
	}

	/**
	 * Block or unblock incoming connections.
	 * 
	 * @param showBlockedPackets show blocked packets.
	 */
	public static void block(boolean showBlockedPackets) {
		block = !block;

		if(block == false) {
			DefaultLogger.appendColoredText("[removed block from incoming packets]", Color.GRAY);
			
			synchronized(blockedPackets) {
				if(showBlockedPackets) {
					DefaultLogger.appendColoredText("[showing blocked packets...]", Color.GRAY);
	
					for(int i = 0; i < blockedPackets.size(); i++) {
						Object [] obj = blockedPackets.get(i);
						NetworkTypes networkType = (NetworkTypes) obj[0];
						Packet packet = (Packet) obj[1];
						DefaultLogger.appendColoredText("["+obj[2]+"/"+blockedPacketCount+"]", Color.DARK_GRAY);
						parsePacket(networkType, packet);
					}
					
					DefaultLogger.appendColoredText("[...end of blocked packets]", Color.GRAY);
				}
				
				blockedPackets.clear();
				blockedPacketCount = 0;
			}
		} else
		if(block == true) {
			DefaultLogger.appendColoredText("[blocking incoming packets]", Color.GRAY);
		}
	}
	
	/**
	 * Display the network status.
	 */
	public static void status() {
		if(Client.isRunning()) {
			DefaultLogger.appendColoredText("[client connected to Server]", Color.CYAN);
		}
		else
		if(Server.isRunning()) {
			try {
				DefaultLogger.appendColoredText("[Server open at "+InetAddress.getLocalHost().getHostAddress()+"]", Color.CYAN);
			} catch (UnknownHostException e) {
				DefaultLogger.appendColoredText("[Server status unknown]", Color.RED);
			}
		}
		else {
			SoundPlayer.play("error");
			DefaultLogger.appendColoredText("[no network detected]", Color.RED);
		}
	}
	
	/**
	 * Check to see if either the server or client is running.
	 */
	private static void checkNetwork() throws AlreadyRunningNetworkException {
		if(Server.isRunning() || Client.isRunning()) {
			SoundPlayer.play("error");
			DefaultLogger.appendColoredText("[network already running]", Color.RED);
			throw new AlreadyRunningNetworkException("You are already running a network!");
		}
	}
	
	/**
	 * Close down connections.
	 */
	public static void close() {
		if(Client.isRunning()) {
			Client.sendPacket(new Packet02Disconnect("[client is disconnecting...]"));
			
			SoundPlayer.play("servoEject");
			Action clientDisconnect = new Action() {
				public void pre() {
					SoundPlayer.play("tapeInsert");
				}
				public void execute() {
					Client.disconnect();
				}
			};
			
			NotificationUI.queueNotification("CLIENT DISCONNECTING", 600, clientDisconnect, true);
		}
		else
		if(Server.isRunning()) {
			DefaultLogger.appendColoredText("[server closing...]", Color.GRAY);
			Server.sendPacketToAllClients(new Packet02Disconnect("[server is closing...]"));
			
			SoundPlayer.play("servoEject");
			Action serverDisconnect = new Action() {
				public void pre() {
					SoundPlayer.play("tapeInsert");
				}
				public void execute() {
					Server.close();
				}
				public void post() {
					StatusUI.removeStatusDisplay(NotificationUI.getPanel());
				}
			};
			
			NotificationUI.queueNotification("SERVER CLOSING", 600, serverDisconnect, true);
		}
		else {
			SoundPlayer.play("error");
			DefaultLogger.appendColoredText("[no network detected]", Color.RED);
		}
	}
	
}
