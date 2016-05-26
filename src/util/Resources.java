package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
	public static final String DIRECTORY;

	public static final Dimension SCREEN_SIZE;
	
	public static final int WINDOW_HEIGHT;
	public static final int WINDOW_WIDTH;
	public static final int RENDER_SPEED;

	public static final Font USER_INPUT;
	public static final Font DOS;
	public static final Font MAN_DISPLAY;
	
	public static final Color CONSOLE_RED;
	public static final Color CONSOLE_GREEN;
	
	public static final String BAN_LIST;
	public static final String BUG_LIST;
	public static final String DEV_COMMANDLIST;
	public static final String TAG_LIST;
	public static final String MAN_LIST;
	public static final String USER_COMMANDLIST;
	public static final String HELP;
	public static final String REFERENCE;
	public static final String SOUND;
	public static final String CURRENT_VERSION;
	
	public static final String[] BAN_LIST_ENTRIES;
	
	public static final ArrayList<String> USER_COMMANDLIST_ENTRIES;
	public static final ArrayList<String> DEV_COMMANDLIST_ENTRIES;
	public static final ArrayList<String> MAN_LIST_ENTRIES;
	public static final ArrayList<String> TAG_LIST_ENTRIES;
	
	public static ArrayList<String> all_versions;
	public static ArrayList<String> master_commandList;
	public static ArrayList<String> tempBanList;

	public static final Image terminalBG;
	public static final Image notesBG;
	
	// Prevent object instantiation
	private Resources() {}
		
	static {
		DIRECTORY = "";
		
		SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		
		WINDOW_HEIGHT = 800;
		WINDOW_WIDTH = 1300;
		RENDER_SPEED = 80;
		
		USER_INPUT = new Font("Courier", Font.PLAIN, 12);
		DOS = loadFont(DIRECTORY+"src/files/fonts/DOS.ttf").deriveFont(13f);
		MAN_DISPLAY = loadFont(DIRECTORY+"src/files/fonts/Trebuchet.ttf").deriveFont(13f);
		
		CONSOLE_RED = new Color(185, 0, 15);
		CONSOLE_GREEN = new Color(15, 170, 0);
		
		BAN_LIST = "src/files/reference/lists/developer/banlist.txt";
		BUG_LIST = "src/files/reference/lists/developer/buglist.txt";
		DEV_COMMANDLIST = "src/files/reference/lists/developer/developer_commandlist.txt";
		TAG_LIST = "src/files/reference/lists/developer/taglist.txt";
		MAN_LIST = "src/files/reference/lists/user/manlist.txt";
		USER_COMMANDLIST = "src/files/reference/lists/user/user_commandlist.txt";
		HELP = "src/files/reference/help.txt";
		REFERENCE = "src/files/reference/reference.txt";
		SOUND = "src/files/reference/sound.txt";
		
		BAN_LIST_ENTRIES = parseTextFromFile(DIRECTORY+BAN_LIST, "\\s+");
		
		USER_COMMANDLIST_ENTRIES = new ArrayList<String>(Arrays.asList(parseTextFromFile(DIRECTORY+USER_COMMANDLIST, "\n")));
		DEV_COMMANDLIST_ENTRIES = new ArrayList<String>(Arrays.asList(parseTextFromFile(DIRECTORY+DEV_COMMANDLIST, "\n")));
		MAN_LIST_ENTRIES = getLexicographicalText(DIRECTORY+MAN_LIST, "<EOF>");
		TAG_LIST_ENTRIES = new ArrayList<String>(Arrays.asList(parseTextFromFile(DIRECTORY+TAG_LIST, "\n")));

		all_versions = new ArrayList<String>();
		master_commandList = new ArrayList<String>();
		master_commandList.addAll(USER_COMMANDLIST_ENTRIES);
		master_commandList.addAll(DEV_COMMANDLIST_ENTRIES);
		tempBanList = new ArrayList<String>();
		
		terminalBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/images/gifs/terminal.gif");
		notesBG = Toolkit.getDefaultToolkit().getImage(DIRECTORY+"src/files/images/gifs/notes.gif");
		
		CURRENT_VERSION = loadVersion(DIRECTORY+REFERENCE);
		loadActionWords(DIRECTORY+"src/files/Actions.txt");
		loadPlaceWords(DIRECTORY+"src/files/Places.txt");
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
		for(String word : parseTextFromFile(filePath, "\\s+"))
			Colorer.addWord(word, cr);
	}
	
	/**
	 * Loads the VERISON.
	 */
	private static String loadVersion(String filePath) {
		String version = null;
		
		String[] lines = parseTextFromFile(filePath, "\\s+");
		for(int i = 0; i < lines.length-1; i++)
			if(lines[i].equalsIgnoreCase("version:")) {
				version = lines[i+1];
				break;
			}
		
		DecimalFormat formatter = new DecimalFormat("00");
		int versionNumber = Integer.parseInt(version.substring(3));
		for(int i = versionNumber; i > 0; i--)
			all_versions.add("v1."+formatter.format(i));
		
		return version;
	}
	
	/**
	 * Parse text from file, ignoring any lines starting with "//".
	 * 
	 * @param filePath the path of the file.
	 * @param delimter the delimiter to split the {@code String}.
	 * @return array split by "\n"
	 */
	public static String[] parseTextFromFile(String filePath, String delimiter) {
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
	 * Writes formatted text to a file.
	 * 
	 * @param filePath the path to the file.
	 * @param text the text to write.
	 * @param format the format to write the text.
	 * @param append whether or not to append to the file.
	 */
	public static void writeTextToFile(String filePath, String text, boolean append) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(filePath), append);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedWriter bw = new BufferedWriter(fw);
		
		try {
			bw.write(text);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an ArrayList of lexicographically sorted text, but only where
	 * marked with SORT tags.
	 * 
	 * @param filePath the file to sort.
	 * @return an ArrayList containing sorted lines.
	 */
	public static ArrayList<String> getLexicographicalText(String filePath, String delimeter) {
		ArrayList<String> toSort = new ArrayList<String>();
		String [] text = parseTextFromFile(filePath, delimeter);
		
		int startSort = -1;
		int endSort = -1;
		int i;
		
		for(i = 0; i < text.length; i++)
			if(text[i].trim().startsWith("<SORT>")) {
				startSort = endSort = i;
				break;
			}
		
		while(++i < text.length && !text[i].trim().startsWith("</SORT>"))
			endSort++;
		
		if(!text[endSort+1].trim().startsWith("</SORT>"))
			return null;
		
		for(i = startSort+1; i < endSort; i++)
			toSort.add(text[i]);
		
		Collections.sort(toSort);
		
		ArrayList<String> sorted = new ArrayList<String>();
		
		for(i = 0; i < startSort; i++)
			sorted.add(text[i]);
		
		sorted.addAll(toSort);
		
		for(i = endSort+2; i < text.length; i++)
			sorted.add(text[i]);
		
		return sorted;
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
	
	/**
	 * Returns the value of a tag with a given ID.
	 * 
	 * @param tagID the tagID in <> format.
	 * @return the name of the tag.
	 */
	public static String getTagString(String tagID) {
		int index = -1;
		
		for(int i = 0; i < TAG_LIST_ENTRIES.size(); i++)
			if(TAG_LIST_ENTRIES.get(i).startsWith(tagID)) {
				index = i;
				break;
			}
		
		if(index == -1)
			return null;
		
		return Resources.TAG_LIST_ENTRIES.get(index).split("=")[1].trim();
	}
	
	/**
	 * Returns if a String is a tag.
	 * 
	 * @param str the tagId in <> format.
	 * @return if the tag exists.
	 */
	public static boolean isTag(String str) {
		for(int i = 0; i < TAG_LIST_ENTRIES.size(); i++)
			if(TAG_LIST_ENTRIES.get(i).startsWith(str))
				return true;
		
		return false;
	}
	
}
