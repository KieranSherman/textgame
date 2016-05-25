package network.server;

import network.upnp.UPnPGateway;

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
		UPnPGateway.setOverwriteExisting(true);
		
		new Thread() {
			public void run() {
				UPnPGateway.removeMapAtPort(port);
			}
		}.start();
	}
	
	/**
	 * Adds a UPnP map to the local machine at a given port.
	 * 
	 * @param port the port to add UPnP mapping to.
	 */
	public static void addUPnPMapAtPort(int port) {
		new Thread() {
			public void run() {
				UPnPGateway.openGatewayAtPort(port);
			}
		}.start();
	}
	
	/**
	 * Sets whether or not the UPnP can ovewrite an existing map.
	 * 
	 * @param remap overwrite an existing map.
	 */
	public static void setUPnPRemap(boolean remap) {
		UPnPGateway.setOverwriteExisting(remap);
	}
	
}
