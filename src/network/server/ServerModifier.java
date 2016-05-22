package network.server;

import java.awt.Color;

import network.upnp.UPnPGateway;
import util.out.DefaultLogger;

public class ServerModifier {
	
	private ServerModifier() {}

	public static void setLocalHostMaximum(int localHostMaximum) {
		Server.localHostMaximum = localHostMaximum; 
	}
	
	public static void setSameClientMaximum(int sameClientMaximum) {
		Server.sameClientMaximum = sameClientMaximum;
	}
	
	public static void setClientConnectionMaximum(int clientConnectionMaximum) {
		Server.clientConnectionMaximum = clientConnectionMaximum;
	}
	
	public static void removeUPnPMapAtPort(int port) {
		if(!Server.isRunning()) {
			DefaultLogger.appendColoredText("[you must be running a server]", Color.RED);
			return;
		}
		
		UPnPGateway.setRemap(true);
		
		try {
			UPnPGateway.removePortMap(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addUPnPMapAtPort(int port) {
		try {
			UPnPGateway.mapToPort(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setUPnPRemap(boolean remap) {
		UPnPGateway.setRemap(remap);
	}
	
}
