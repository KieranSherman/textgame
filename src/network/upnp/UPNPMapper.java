package network.upnp;

import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class UPNPMapper {

	private static int port = 9999;
	private static boolean listAllMappings = false;
	private static GatewayDevice activeGW;
	
	public static void portForward(int port) {
		UPNPMapper.port = port;
		try {
			mapToPort(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		try {
			removePortMap(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void mapToPort(int port) throws Exception {
		addLogLine("Starting UPNP");

		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		addLogLine("Looking for Gateway Devices...");

		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();

		if (gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping weupnp");
			return;
		}
		addLogLine(gateways.size()+" gateway(s) found\n");

		int counter=0;
		for (GatewayDevice gw : gateways.values()) {
			counter++;
			addLogLine("Listing gateway details of device #" + counter+
					"\n\tFriendly name: " + gw.getFriendlyName()+
					"\n\tPresentation URL: " + gw.getPresentationURL()+
					"\n\tModel name: " + gw.getModelName()+
					"\n\tModel number: " + gw.getModelNumber()+
					"\n\tLocal interface address: " + gw.getLocalAddress().getHostAddress()+"\n");
		}

		// choose the first active gateway for the tests
		activeGW = gatewayDiscover.getValidGateway();

		if (null != activeGW) {
			addLogLine("Using gateway: " + activeGW.getFriendlyName());
		} else {
			addLogLine("No active gateway device found");
			addLogLine("Stopping weupnp");
			return;
		}

		// testing getGenericPortMappingEntry
		PortMappingEntry portMapping = new PortMappingEntry();
		if (listAllMappings) {
			int pmCount = 0;
			do {
				if (activeGW.getGenericPortMappingEntry(pmCount, portMapping))
					addLogLine("Portmapping #"+pmCount+" successfully retrieved ("+portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
				else{
					addLogLine("Portmapping #"+pmCount+" retrieval failed"); 
					break;
				}
				pmCount++;
			} while (portMapping != null);
		} else {
			if (activeGW.getGenericPortMappingEntry(0, portMapping))
				addLogLine("Portmapping #0 successfully retrieved ("+portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
			else
				addLogLine("Portmapping #0 retrival failed");        	
		}

		InetAddress localAddress = activeGW.getLocalAddress();
		addLogLine("Using local address: "+ localAddress.getHostAddress());
		String externalIPAddress = activeGW.getExternalIPAddress();
		addLogLine("External address: "+ externalIPAddress);

		addLogLine("Querying device to see if a port mapping already exists for port "+ port);

		if (activeGW.getSpecificPortMappingEntry(port, "TCP", portMapping)) {
			addLogLine("Port "+port+" is already mapped. Aborting test.");
			return;
		} else {
			addLogLine("Mapping free. Sending port mapping request for port "+port);

			// test static lease duration mapping
			if (activeGW.addPortMapping(port, port, localAddress.getHostAddress(), "TCP", "test"))
				addLogLine("Mapping SUCCESSFUL.");
		}
	}
	
	private static void removePortMap(int port) throws Exception {
		addLogLine("Stopping UPNP");
		
		if(activeGW.deletePortMapping(port, "TCP")) {
			addLogLine("Port mapping removal SUCCESSFUL");
        } else {
			addLogLine("Port mapping removal FAILED");
        }
	}

	private static void addLogLine(String line) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line+"\n";
		System.out.print(logline);
	}

}
