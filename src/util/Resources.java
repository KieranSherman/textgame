package util;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.Colorer.ColorRules;

public class Resources {
	// height and width of window screen.  1.618 = phi (golden rectangle)
	public final static int HEIGHT = 400, WIDTH = (int)(HEIGHT*1.618);
	public final static int BEVEL = 5;
	public final static Font def = new Font("Courier", Font.PLAIN, 12);
	public static Colorer colorer;
	
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void init() {
		colorer = new Colorer();
		
		loadActionWords("src/Actions_WR.txt");
		loadPlaceWords("src/Places_WR.txt");
	}
	
	public static void loadActionWords(String filePath) {
		loadWords(filePath, ColorRules.ACTION);
	}
	
	public static void loadPlaceWords(String filePath) {
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

}
