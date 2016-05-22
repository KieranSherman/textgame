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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import util.out.Colorer;
import util.out.Colorer.ColorRules;

/**
 * Class consists exclusively of static methods and variables which are accessed by numerous classes.
 * 
 * @author kieransherman
 *
 */
public class Resources {
	public static String DIRECTORY = "";

	public final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public final static int HEIGHT = 800, WIDTH = 1300;	//height and width of window
	
	public final static Font USER_INPUT = new Font("Courier", Font.PLAIN, 12);	//font for user output
	public final static Font DOS;
	
	public final static Color DARK_RED = new Color(185, 0, 15);		//dark red color
	public final static Color DARK_GREEN = new Color(15, 170, 0);	//dark green color
	
	public final static String VERSION;
	
	public final static String[] BANLIST;
	public static ArrayList<String> MASTER_COMMANDLIST;
	public final static ArrayList<String> USER_COMMANDLIST;
	public final static ArrayList<String> DEV_COMMANDLIST;

	public static ArrayList<String> tempBanList;
	
	public static final int RENDER_SPEED = 80;
	public static Image commandBG, terminalBG, devterminalBG, notesBG;
	
	// Prevent object instantiation
	private Resources() {}
	
	static {
		loadActionWords(DIRECTORY+"src/files/Actions.txt");
		loadPlaceWords(DIRECTORY+"src/files/Places.txt");
		
		commandBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/command.gif");
		terminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/terminal.gif");
		devterminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/devterminal.gif");
		notesBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/notes.gif");
		
		VERSION = loadVersion(DIRECTORY+"src/files/reference/reference.txt");
		DOS = loadFont(DIRECTORY+"src/files/fonts/DOS.ttf").deriveFont(13f);
		
		BANLIST = parseText(DIRECTORY+"src/files/reference/lists/banlist.txt", "\\s+");
		USER_COMMANDLIST = new ArrayList<String>(Arrays.asList(parseText(DIRECTORY+"src/files/reference/lists/user_commandlist.txt", "\n")));
		DEV_COMMANDLIST = new ArrayList<String>(Arrays.asList(parseText(DIRECTORY+"src/files/reference/lists/developer_commandlist.txt", "\n")));
		
		MASTER_COMMANDLIST = new ArrayList<String>();
		MASTER_COMMANDLIST.addAll(USER_COMMANDLIST);
		MASTER_COMMANDLIST.addAll(DEV_COMMANDLIST);
		
		tempBanList = new ArrayList<String>();
	}
	
	/**
	 * Loads a font from filePath.
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
	
	/**
	 * Load the action words.
	 */
	private static void loadActionWords(String filePath) {
		loadWords(filePath, ColorRules.ACTION);
	}
	
	/**
	 * Load the place words.
	 */
	private static void loadPlaceWords(String filePath) {
		loadWords(filePath, ColorRules.PLACE);
	}
	
	/**
	 * Load Word objects into Colorer.
	 */
	private static void loadWords(String filePath, ColorRules cr) {
		for(String word : parseText(filePath, "\\s+"))
			Colorer.addWord(word, cr);
	}
	
	/**
	 * Loads the VERISON.
	 */
	private static String loadVersion(String filePath) {
		String[] lines = parseText(filePath, "\\s+");
		for(int i = 0; i < lines.length-1; i++)
			if(lines[i].equalsIgnoreCase("version:"))
				return lines[i+1];
		
		return null;
	}
	
	/**
	 * Parse text from file, ignoring any lines starting with "//".
	 * 
	 * @param filePath the path of the file.
	 * @param delimter the delimiter to split the {@code String}.
	 * @return array split by "\n"
	 */
	public static String[] parseText(String filePath, String delimiter) {
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
		
		return text.split(delimiter);
	}
	
	/**
	 * Returns a TitledBorder.
	 * 
	 * @param title the title.
	 * @param titleColor the title's color.
	 * @return the border.
	 */
	public static Border getBorder(String title, Color titleColor) {
		Border linedBorder, titledBorder;
		
		linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		titledBorder = BorderFactory.createTitledBorder(linedBorder, title, 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), titleColor);
		
		return titledBorder;
	}
	
}
