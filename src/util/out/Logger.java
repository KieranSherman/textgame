package util.out;

import main.Window;

/*
 * Logger class has direct access to the Game's Window
 */
public class Logger {
	
	private static Window window;
	
	public Logger(Window window) {
		Logger.window = window;
	}
	
	public void appendText(String str) {
		window.appendText(str);
	}
	
	
}
