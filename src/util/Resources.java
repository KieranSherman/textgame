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
import javax.swing.JFileChooser;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import util.out.Colorer;
import util.out.Colorer.ColorRules;

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
	
	private static String parseDelimiter = "\\s+"; 	//delimiter used to split text in files
	public final static String VERSION;
	
	public final static String[] BANLIST;
	public static ArrayList<String> MASTER_COMMANDLIST;
	public final static ArrayList<String> USER_COMMANDLIST;
	public final static ArrayList<String> DEV_COMMANDLIST;

	public static ArrayList<String> tempBanList;
	
	public static final int RENDER_SPEED = 80;
	public static Image commandBG, terminalBG, devterminalBG, notesBG;
	
	private Resources() {}							//prevent instantiation of Resources object
	
	static {
		loadActionWords(DIRECTORY+"src/files/Actions.txt");
		loadPlaceWords(DIRECTORY+"src/files/Places.txt");
		
		commandBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/command.gif");
		terminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/terminal.gif");
		devterminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/devterminal.gif");
		notesBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/imgs/gifs/notes.gif");
		
		VERSION = loadVersion(DIRECTORY+"src/files/reference/reference.txt");
		DOS = loadFont(DIRECTORY+"src/files/fonts/DOS.ttf").deriveFont(13f);
		
		BANLIST = parseText(DIRECTORY+"src/files/reference/lists/banlist.txt");
		USER_COMMANDLIST = new ArrayList<String>(Arrays.asList(parseText(DIRECTORY+"src/files/reference/lists/user_commandlist.txt")));
		DEV_COMMANDLIST = new ArrayList<String>(Arrays.asList(parseText(DIRECTORY+"src/files/reference/lists/developer_commandlist.txt")));
		
		MASTER_COMMANDLIST = new ArrayList<String>();
		MASTER_COMMANDLIST.addAll(USER_COMMANDLIST);
		MASTER_COMMANDLIST.addAll(DEV_COMMANDLIST);
		
		tempBanList = new ArrayList<String>();
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
			Colorer.addWord(word, cr);
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
	
	public static Border getBorder(String title, Color titleColor) {
		Border linedBorder, titledBorder, compoundBorder;
		
		linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		titledBorder = BorderFactory.createTitledBorder(linedBorder, title, 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), titleColor);
		compoundBorder = BorderFactory.createCompoundBorder(titledBorder, null);
		
		return compoundBorder;
	}
	
}
