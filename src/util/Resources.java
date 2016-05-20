package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import network.Adapter;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;
import util.out.Colorer.ColorRules;
import util.out.Logger;

/*
 * Class holds resources used throughout classes
 */
public class Resources {
	public static String DIRECTORY = "";

	public final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public final static int HEIGHT = 800, WIDTH = 1300;	//height and width of window
	
	public final static Font USER_INPUT = new Font("Courier", Font.PLAIN, 12);	//font for user output
	public final static Font DOS;
	
	public final static Color DARK_RED = new Color(185, 0, 15);		//dark red color
	public final static Color DARK_GREEN = new Color(15, 170, 0);	//dark green color
	
	private static Colorer colorer;					//Colorer object to color user input
	private static Logger logger;					//Logger to append text to user output window
	private static Adapter adapter;					//Adapter to bridge between network and UI
	
	private static String parseDelimiter = "\\s+"; 	//delimiter used to split text in files
	public final static String VERSION;
	
	public static final int RENDER_SPEED = 80;
	public static Image commandBG, terminalBG, devterminalBG, notesBG;
	
	private Resources() {}							//prevent instantiation of Resources object
	
	static {
		colorer = new Colorer();
		logger = new Logger();
		adapter = new Adapter();
		
		loadActionWords(DIRECTORY+"src/files/Actions.txt");
		loadPlaceWords(DIRECTORY+"src/files/Places.txt");
		commandBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/command.gif");
		terminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/terminal.gif");
		devterminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/devterminal.gif");
		notesBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/notes.gif");
		VERSION = loadVersion(DIRECTORY+"src/files/reference/reference.txt");
		DOS = loadFont(DIRECTORY+"src/files/fonts/DOS.ttf").deriveFont(13f);
	}
	
	public static void installer() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showOpenDialog(null);
		
		if(returnValue == JFileChooser.CANCEL_OPTION) {
			System.exit(0);
		} else if (returnValue == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			DIRECTORY = f.getAbsolutePath()+"/";
		}
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
