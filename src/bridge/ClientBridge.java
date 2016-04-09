package bridge;

import network.Client;

public class ClientBridge extends Bridge {
	
	private Client client;
	
	public ClientBridge(Client client) {
		super();
		this.client = client;
	}

	@Override
	//to be used from Client.class
	public void writeObject(Object obj) {
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		client.writeObject(obj);
	}

	@Override
	//to be used from Client.class
	public <T> T readObject() {
		Object obj = client.readObject();
		
		if(obj instanceof String)
			super.logger.appendText((String) obj);
		
		return client.readObject();
	}

}
