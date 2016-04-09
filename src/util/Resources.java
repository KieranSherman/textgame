package util;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import main.Window;
import util.bridge.StreamCapturer;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;
import util.out.Logger;
import util.out.Colorer.ColorRules;

public class Resources {
	// height and width of window screen.  1.618 = phi (golden rectangle)
	public final static int HEIGHT = 400, WIDTH = (int)(HEIGHT*1.618);
	public final static int BEVEL = 5;
	public final static Font def = new Font("Courier", Font.PLAIN, 12);

	private static Colorer colorer;
	private static Logger logger;
	
	private static boolean initialized = false;
	
	public static void init(Window window) {
		colorer = new Colorer();
		logger = new Logger(window);
		
		loadActionWords("src/files/Actions.txt");
		loadPlaceWords("src/files/Places.txt");
		overrideOutput();
		
		initialized = true;
	}
	
	private static void overrideOutput() {
		PrintStream stdOut = System.out;
		PrintStream stdErr = System.err;
		
        System.setOut(new PrintStream(new StreamCapturer("STDOUT", logger, stdOut)));
        System.setErr(new PrintStream(new StreamCapturer("STDERR", logger, stdErr)));
	}
	
	private static void loadActionWords(String filePath) {
		loadWords(filePath, ColorRules.ACTION);
	}
	
	private static void loadPlaceWords(String filePath) {
		loadWords(filePath, ColorRules.PLACE);
	}
	
	private static void loadWords(String filePath, ColorRules cr) {
		for(String word : parseText(filePath))
			colorer.addWord(word, cr);
	}
	
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
			while((line = br.readLine()) != null)
				text += line+"\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text.split(",");
	}
	
	public static Colorer getColorer() throws ResourcesNotInitializedException {
		if(!checkInit())
			return null;
		
		return colorer;
	}
	
	public static Logger getLogger() throws ResourcesNotInitializedException {
		if(!checkInit())
			return null;
		
		return logger;
	}

	private static boolean checkInit() throws ResourcesNotInitializedException {
		if(!initialized)
			throw new ResourcesNotInitializedException("You must initialize Resources.class before using it!");
		
		return initialized;
	}
}
