package util.out;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;

import main.ui.Developer;

/**
 * Outputs data with timestamp to standard out and window if developer mode is enabled
 */
public class DeveloperLogger {
	
	private DeveloperLogger() {}
	
	public static void appendText(String line) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line;
		
		if(Developer.isDeveloperModeEnabled())
			DefaultLogger.appendColoredText("["+logline+"]", Color.GREEN);
		
		System.out.println(logline);
	}
	
	public static void appendColoredText(String line, Color color) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line;
		
		if(Developer.isDeveloperModeEnabled())
			DefaultLogger.appendColoredText("["+logline+"]", color);
		
		System.out.println(logline);
	}

}
