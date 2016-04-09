package bridge;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import util.Logger;
import util.Resources;
import util.ResourcesNotInitializedException;

/*
 * Class bridges the GUI and the Network
 */
public abstract class Bridge extends OutputStream {
	
	protected Logger logger;
	
	public Bridge() {
		try {
			logger = Resources.getLogger();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		overrideOutput();
	}
	
	@Override
    public void write(int b) throws IOException {
        appendText(String.valueOf((char)b));
    }
	
	//to be used from Window.class
	public void appendText(String str) {
		logger.appendText(str);
	}
	
	public abstract void writeObject(Object obj);
	public abstract <T> T readObject();
	
	private void overrideOutput() {
		PrintStream printStream = new PrintStream(this);
		
		System.setOut(printStream);
		System.setErr(printStream);
	}
}
