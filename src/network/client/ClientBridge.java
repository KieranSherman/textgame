package network.client;

import network.Bridge;

public class ClientBridge extends Bridge {
	
	private Client client;
	
	public ClientBridge(Client client) {
		super();
		this.client = client;
	}

	@Override
	public void writeObject(Object obj) {
		client.writeObject(obj);
	}

	@Override
	public <T> T readObject() {
		Object obj = client.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return client.readObject();
	}

}