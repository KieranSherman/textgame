package network.bridge;

import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

/*
 * Class bridges the GUI and the Network
 */
public abstract class Bridge {
	
	public enum BridgeType {
		CLIENT, SERVER;
	}
	
	protected Logger logger;
	
	public Bridge() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//to be used from Window.class
	public void appendText(String str) {
		logger.appendText(str);
	}
	
	public abstract void writeObject(Object obj);
	public abstract <T> T readObject();
}
