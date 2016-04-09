package bridge;

import network.Server;

public class ServerBridge extends Bridge {
	
	private Server server;
	
	public ServerBridge(Server server) {
		super();
		this.server = server;
	}

	@Override
	//to be used from Server.class
	public void writeObject(Object obj) {
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		server.writeObject(obj);
	}

	@Override
	//to be used from Server.class
	public <T> T readObject() {
		Object obj = server.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return server.readObject();
	}

}
