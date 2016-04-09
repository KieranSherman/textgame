package network;

import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Logger;

/*
 * Class bridges the GUI and the Network
 */
public abstract class Bridge {
	
	protected Logger logger;
	
	public Bridge() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public abstract void writeObject(Object obj);
	public abstract <T> T readObject();
}
