package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import network.Adapter;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;
import util.out.Colorer.ColorRules;
import util.out.Logger;

/*
 * Class holds resources used throughout classes
 */
public class Resources {
	public final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public final static int HEIGHT = (int)screenSize.getHeight()-50, WIDTH = (int)screenSize.getWidth()-20;	//height and width of window
	
	public final static Font USER_INPUT = new Font("Courier", Font.PLAIN, 12);	//font for user output
	public final static Font DOS = loadFont("src/files/fonts/DOS.ttf").deriveFont(13f);
	
	public final static Color DARK_RED = new Color(185, 0, 15);		//dark red color
	public final static Color DARK_GREEN = new Color(15, 170, 0);	//dark green color
	
	private static Colorer colorer;					//Colorer object to color user input
	private static Logger logger;					//Logger to append text to user output window
	private static Adapter adapter;					//Adapter to bridge between network and UI
	
	private static String parseDelimiter = "\\s+"; 	//delimiter used to split text in files
	public static String VERSION = loadVersion("src/files/reference/Reference.txt");
	public static String HOST_ADDRESS;
	
	public static final int RENDER_SPEED = 80;
	
	public static final BufferedImage bootImage = loadImage("src/files/imgs/pngs/booting1.png");
	
	public static final Image commandBG = Toolkit.getDefaultToolkit().getImage("src/files/imgs/gifs/command.gif");
	public static final Image terminalBG = Toolkit.getDefaultToolkit().getImage("src/files/imgs/gifs/terminal.gif");
	public static final Image notesBG = Toolkit.getDefaultToolkit().getImage("src/files/imgs/gifs/notes.gif");
	
	private Resources() {}							//prevent instantiation of Resources object
	
	static {
		try {
			HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		colorer = new Colorer();
		logger = new Logger();
		adapter = new Adapter();
		
		loadActionWords("src/files/Actions.txt");
		loadPlaceWords("src/files/Places.txt");
	}
	
	/*
	 * Loads font from filePath
	 */
	private static Font loadFont(String filePath) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(filePath));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * Load the action words
	 */
	private static void loadActionWords(String filePath) {
		loadWords(filePath, ColorRules.ACTION);
	}
	
	/*
	 * Load the place words
	 */
	private static void loadPlaceWords(String filePath) {
		loadWords(filePath, ColorRules.PLACE);
	}
	
	/*
	 * Load Word objects into Colorer
	 */
	private static void loadWords(String filePath, ColorRules cr) {
		for(String word : parseText(filePath))
			colorer.addWord(word, cr);
	}
	
	/*
	 * Loads the VERISON String from the Class Hierarchy
	 */
	private static String loadVersion(String filePath) {
		String[] lines = parseText(filePath);
		for(int i = 0; i < lines.length-1; i++)
			if(lines[i].equalsIgnoreCase("version:"))
				return lines[i+1];
		
		return null;
	}
	
	private static BufferedImage loadImage(String filePath) {
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			System.err.println("error loading image");
		}
		
		return null;
	}
	
	/*
	 * Parse text from a file at filePath
	 */
	private static String[] parseText(String filePath) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		String line, text = "";
		
		try {
			while((line = br.readLine()) != null) {
				if(line.indexOf("//") != -1)
					line = line.substring(0, line.indexOf("//"));
				
				text += line += "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text.split(parseDelimiter);
	}
	
	/*
	 * Parse text from a file at filePath
	 */
	public static String[] readText(String filePath) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		String line, text = "";
		
		try {
			while((line = br.readLine()) != null)
				text += line += "\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text.split("\n");
	}
	
	/*
	 * Returns the colorer
	 */
	public static Colorer getColorer() throws ResourcesNotInitializedException {
		return colorer;
	}
	
	/*
	 * Returns the logger
	 */
	public static Logger getLogger() throws ResourcesNotInitializedException {
		return logger;
	}
	
	/*
	 * Returns the adapter
	 */
	public static Adapter getAdapter() throws ResourcesNotInitializedException {
		return adapter;
	}

}
