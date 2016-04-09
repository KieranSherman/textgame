package network.server;

import network.Bridge;

public class ServerBridge extends Bridge {
	
	private Server server;
	
	public ServerBridge(Server server) {
		super();
		this.server = server;
	}

	@Override
	public void writeObject(Object obj) {
		server.writeObject(obj);
	}

	@Override
	public <T> T readObject() {
		Object obj = server.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return server.readObject();
	}

}
