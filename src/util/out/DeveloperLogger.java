package util.out;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;

import main.Developer;
import main.ui.components.display.DisplayUI;

/**
 * Class consists exclusively of static methods which append text to the {@link DisplayUI}
 * and to standard out.
 * 
 * @author kieransherman
 *
 */
public class DeveloperLogger {
	
	// Prevent object instantiation
	private DeveloperLogger() {}
	
	/**
	 * Append text to the end of the display and standard out.
	 * 
	 * @param line the {@code String} to append.
	 */
	public static void appendText(String line) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line;
		
		if(Developer.isDeveloperModeEnabled())
			DefaultLogger.appendColoredText("["+logline+"]", Color.GREEN);
		
		System.out.println(logline);
	}
	
	/**
	 * Append colored text to the end of the display and standard out.
	 * 
	 * @param line the {@code String} to append.
	 * @param color the color of the line.
	 */
	public static void appendColoredText(String line, Color color) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line;
		
		if(Developer.isDeveloperModeEnabled())
			DefaultLogger.appendColoredText("["+logline+"]", color);
		
		System.out.println(logline);
	}

}
