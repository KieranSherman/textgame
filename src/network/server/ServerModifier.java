package network.server;

import java.awt.Color;

import network.upnp.UPNnGateway;
import util.out.DefaultLogger;

/**
 * Class consists of exclusively static methods and modifies the server.
 * 
 * @author kieransherman
 * 
 */
public class ServerModifier {
	
	// Prevent object instantiation.
	private ServerModifier() {}

	/**
	 * Sets the maximum number of localhost connections allowed.
	 * 
	 * @param localHostMaximum the maximum number of connections.
	 */
	public static void setLocalHostMaximum(int localHostMaximum) {
		Server.localHostMaximum = localHostMaximum; 
	}
	
	/**
	 * Sets the maximum number of connections allowed from the same client.
	 * 
	 * @param sameClientMaximum the maximum number of connections.
	 */
	public static void setSameClientMaximum(int sameClientMaximum) {
		Server.sameClientMaximum = sameClientMaximum;
	}
	
	/**
	 * Sets the maximum number of connections allowed from clients.
	 * 
	 * @param clientConnectionMaximum the maximum number of connections.
	 */
	public static void setClientConnectionMaximum(int clientConnectionMaximum) {
		Server.clientConnectionMaximum = clientConnectionMaximum;
	}
	
	/**
	 * Removes a UPnP map at a given port.
	 * 
	 * @param port the port to remove UPnP mapping.
	 */
	public static void removeUPnPMapAtPort(int port) {
		if(!Server.isRunning()) {
			DefaultLogger.appendColoredText("[you must be running a server]", Color.RED);
			return;
		}
		
		UPNnGateway.setRemap(true);
		
		try {
			UPNnGateway.removePortMap(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a UPnP map to the local machine at a given port.
	 * 
	 * @param port the port to add UPnP mapping to.
	 */
	public static void addUPnPMapAtPort(int port) {
		try {
			UPNnGateway.mapToPort(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets whether or not the UPnP can ovewrite an existing map.
	 * 
	 * @param remap overwrite an existing map.
	 */
	public static void setUPnPRemap(boolean remap) {
		UPNnGateway.setRemap(remap);
	}
	
}
