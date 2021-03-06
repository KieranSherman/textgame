package network.upnp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

import main.ui.components.popup.PopupUI;
import network.upnp.components.PortMappingEntry;
import network.upnp.components.gateway.GatewayDevice;
import network.upnp.components.gateway.GatewayDiscover;
import util.out.DeveloperLogger;

/**
 * Class consists exclusively of static methods that operate a UPnP gateway.
 * 
 * @author kieransherman
 *
 */
public class UPnPGateway {

	private static ArrayList<Integer> ports;
	private static boolean listAllMappings;
	private static boolean gatewayOpen;
	private static boolean overwriteExisting;
	private static String mappedAddress;
	private static String localAddress;
	
	static {
		ports = new ArrayList<Integer>();
	}
	
	// Prevent object instantiation
	private UPnPGateway() {}
	
	/**
	 * Opens a UPnP gateway to the local machine at a given port.
	 * 
	 * @param port the port to open.
	 */
	public static void openGatewayAtPort(int port) {
		try {
			mapToPort(port);
			addLogLine("added UPnP map at port "+port);
			
			if(!ports.contains(port))
				ports.add(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes a port map at a given port.
	 * 
	 * @param port the port to remove.
	 */
	public static void removeMapAtPort(int port) {
		try {
			removePortMap(port);
			addLogLine("removed map at port "+port);
			
			ports.remove(new Integer(port));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Maps a port to the local machine.
	 * 
	 * @param port the port to open.
	 * @throws Exception
	 */
	private static void mapToPort(int port) throws Exception {
		addLogLine("Starting UPnP");

		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		addLogLine("Looking for gateway devices...");

		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();

		if(gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping UPnP");
			return;
		}
		addLogLine(gateways.size()+" gateway(s) found");

		int counter = 0;
		for(GatewayDevice gw : gateways.values()) {
			counter++;
			addLogLine("Listing gateway details of device #" + counter+
					"\n\tFriendly name: " + gw.getFriendlyName()+
					"\n\tPresentation URL: " + gw.getPresentationURL()+
					"\n\tModel name: " + gw.getModelName()+
					"\n\tModel number: " + gw.getModelNumber()+
					"\n\tLocal interface address: " + gw.getLocalAddress().getHostAddress());
		}

		GatewayDevice activeGW = gatewayDiscover.getValidGateway();

		if(activeGW != null) {
			addLogLine("Using gateway: " + activeGW.getFriendlyName());
		} else {
			addLogLine("No active gateway device found");
			addLogLine("Stopping UPnP");
			return;
		}

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
		addLogLine("Using local address: "+ (UPnPGateway.localAddress = localAddress.getHostAddress()));
		
		mappedAddress = activeGW.getExternalIPAddress();
		addLogLine("External address: "+ mappedAddress);

		addLogLine("Querying device to see if a port mapping already exists for port "+ port);

		if(activeGW.getSpecificPortMappingEntry(port, "TCP", portMapping)) {
			addLogLine("Port "+port+" is already mapped.  Remap? (y/n)");
			
			PopupUI.promptChoice("REMAP->["+localAddress.getHostAddress()+"]", new String[] {"YES", "NO"});
			String choice = (String)PopupUI.getData()[0];
			
			if(choice.equals("YES")) {
				setOverwriteExisting(true);
				addLogLine("Remapping {"+port+"} at ("+mappedAddress+")->("+localAddress+")");
				removePortMap(port);
				mapToPort(port);
			} else {
				addLogLine("Will NOT remap");
			}
			
			return;
		} else {
			addLogLine("Mapping FREE. Sending port mapping request for port "+port);

			if(activeGW.addPortMapping(port, port, localAddress.getHostAddress(), "TCP", "[TEXTGAME SERVER]"))
				addLogLine("Mapping SUCCESSFUL");
		}
		
		gatewayOpen = true;
	}
	
	/**
	 * Removes map at port.
	 * 
	 * @param port the port to remove.
	 * @throws Exception
	 */
	private static void removePortMap(int port) throws Exception {
		if(!overwriteExisting)
			return;
	
		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();

		if(gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping UPnP");
			return;
		}

		GatewayDevice activeGW = gatewayDiscover.getValidGateway();
		PortMappingEntry portMapping = new PortMappingEntry();
		
		activeGW.getGenericPortMappingEntry(0, portMapping);

		if(activeGW.getSpecificPortMappingEntry(port, "TCP", portMapping)) {
			addLogLine("Remapping port "+port);
			if(activeGW.deletePortMapping(port, "TCP")) {
				addLogLine("Port mapping removal: SUCCESSFUL");
			} else
				addLogLine("Port mapping removal: FAILED");
		} else {
			addLogLine("Port at "+port+" is not mapped");
			throw new Exception("Port is not mapped");
		}
	}
	
	/**
	 * Returns the address mapped to the UPnP gateway.
	 * 
	 * @return the address.
	 */
	public static String getMappedAddress() {
		if(!gatewayOpen)
			System.err.println("UPnP gateway not open!");
			
		return mappedAddress;
	}
	
	/**
	 * Returns the address of the local machine.
	 * 
	 * @return the address.
	 */
	public static String getLocalAddress() {
		if(!gatewayOpen)
			System.err.println("UPnP gateway not open!");
			
		return localAddress;
	}
	
	/**
	 * Removes the UPnP gateway and all ports.
	 */
	public static void disconnect() {
		if(!gatewayOpen)
			return;
		
		overwriteExisting = true;
		
		try {
			for(int i = 0; i < ports.size(); i++)
				removePortMap(ports.remove(i));
			
			gatewayOpen = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set whether or not the UPnP mapping will overwrite an existing map.
	 * 
	 * @param remap whether or not to overwite.
	 */
	public static void setOverwriteExisting(boolean remap) {
		UPnPGateway.overwriteExisting = remap;
	}
	
	/**
	 * Add a line to the log.
	 */
	private static void addLogLine(String line) {
		DeveloperLogger.appendText(line);
	}
	
}
