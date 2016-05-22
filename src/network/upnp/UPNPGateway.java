package network.upnp;

import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import main.ui.Developer;
import main.ui.components.popup.PopupUI;
import network.upnp.components.PortMappingEntry;
import network.upnp.components.gateway.GatewayDevice;
import network.upnp.components.gateway.GatewayDiscover;
import util.Resources;
import util.out.Logger;

public class UPNPGateway {

	private static int port = 9999;
	private static boolean listAllMappings;
	private static boolean open;
	private static String mappedAddress;
	private static String localAddress;
	private static GatewayDevice activeGW;
	
	private UPNPGateway() {}
	
	public static void openGatewayAtPort(int port) {
		UPNPGateway.port = port;
		
		try {
			mapToPort(port);
			open = true;
		} catch (Exception e) {
			e.printStackTrace();
			open = false;
		}
	}
	
	public static void disconnect() {
		try {
			removePortMap(port);
			open = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getMappedAddress() {
		if(!open)
			System.err.println("UPNP gateway not open!");
			
		return mappedAddress;
	}
	
	public static String getLocalAddress() {
		if(!open)
			System.err.println("UPNP gateway not open!");
			
		return localAddress;
	}
	
	private static void mapToPort(int port) throws Exception {
		addLogLine("Starting UPNP");

		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		addLogLine("Looking for gateway devices...");

		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();

		if(gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping weupnp");
			return;
		}
		addLogLine(gateways.size()+" gateway(s) found");

		int counter=0;
		for(GatewayDevice gw : gateways.values()) {
			counter++;
			addLogLine("Listing gateway details of device #" + counter+
					"\n\tFriendly name: " + gw.getFriendlyName()+
					"\n\tPresentation URL: " + gw.getPresentationURL()+
					"\n\tModel name: " + gw.getModelName()+
					"\n\tModel number: " + gw.getModelNumber()+
					"\n\tLocal interface address: " + gw.getLocalAddress().getHostAddress());
		}

		// choose the first active gateway for the tests
		activeGW = gatewayDiscover.getValidGateway();

		if(null != activeGW) {
			addLogLine("Using gateway: " + activeGW.getFriendlyName());
		} else {
			addLogLine("No active gateway device found");
			addLogLine("Stopping weupnp");
			return;
		}

		// testing getGenericPortMappingEntry
		PortMappingEntry portMapping = new PortMappingEntry();
		if(listAllMappings) {
			int pmCount = 0;
			do {
				if(activeGW.getGenericPortMappingEntry(pmCount, portMapping))
					addLogLine("Portmapping #"+pmCount+" successfully retrieved ("+
							portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
				else{
					addLogLine("Portmapping #"+pmCount+" retrieval failed"); 
					break;
				}
				pmCount++;
			} while(portMapping != null);
		} else {
			if(activeGW.getGenericPortMappingEntry(0, portMapping))
				addLogLine("Portmapping #0 successfully retrieved ("+
						portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
			else
				addLogLine("Portmapping #0 retrival failed");        	
		}

		InetAddress localAddress = activeGW.getLocalAddress();
		addLogLine("Using local address: "+ (UPNPGateway.localAddress = localAddress.getHostAddress()));
		
		mappedAddress = activeGW.getExternalIPAddress();
		addLogLine("External address: "+ mappedAddress);

		addLogLine("Querying device to see if a port mapping already exists for port "+ port);

		if(activeGW.getSpecificPortMappingEntry(port, "TCP", portMapping)) {
			addLogLine("Port "+port+" is already mapped.  Remap? (y/n)");
			
			PopupUI.promptChoice("REMAP->["+localAddress.getHostAddress()+"]", new String[] {"YES", "NO"});
			String choice = PopupUI.getData();
			
			if(choice.equals("YES")) {
				addLogLine("remapping...");
				removePortMap(port);
				mapToPort(port);
			} else {
				addLogLine("Will not remap");
			}
			
			return;
		} else {
			addLogLine("Mapping free. Sending port mapping request for port "+port);

			if(activeGW.addPortMapping(port, port, localAddress.getHostAddress(), "TCP", "[TEXTGAME SERVER]"))
				addLogLine("Mapping SUCCESSFUL");
		}
	}
	
	private static void removePortMap(int port) throws Exception {
		if(activeGW == null)
			return;
		
		addLogLine("Stopping UPNP");
		
		if(activeGW.deletePortMapping(port, "TCP")) {
			addLogLine("Port mapping removal: SUCCESSFUL");
        } else {
			addLogLine("Port mapping removal: FAILED");
        }
	}

	private static void addLogLine(String line) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line;
		
		if(Developer.isDeveloperModeEnabled())
			Logger.appendColoredText("["+logline+"]", Resources.DARK_GREEN);
		
		System.out.println(logline);
	}

}
