package util.out;

import java.awt.Color;
import java.util.ArrayList;

import network.packet.types.PacketTypes;
import util.Word;

/*
 * Class holds Words and determines their color 
 */
public class Colorer {
	
	private static ArrayList<Word> words = new ArrayList<Word>();
	
	private Colorer() {}
	
	/*
	 * Add a word to the list
	 */
	public static void addWord(Word word) {
		if(word.getWord().startsWith(":") || word.getWord().equals(""))
			return;
		
		words.add(word);
	}
	
	/*
	 * Add a word to the list with a specific ColorRule
	 */
	public static void addWord(String word, ColorRules cr) {
		if(word.startsWith(":") || word.equals(""))
			return;
		
		words.add(new Word(word, cr));
	}
	
	/*
	 * Return the color of the word
	 */
	public static Color getColor(String str) {
		for(Word word : words)
			if(word.equals(str))
				return word.getColor();
		
		return Color.WHITE;
	}
	
	public static Color getPacketColor(PacketTypes packetType) {
		switch(packetType) {
			case ACTION: {
				return Color.WHITE;
			}
			case DISCONNECT: {
				return Color.GRAY;
			}
			case LOGIN: {
				return Color.GREEN;
			}
			case MESSAGE: {
				return Color.MAGENTA;
			}
		}
		
		return null;
	}
	
	/*
	 * Coloring rules for different types of words
	 */
	public static enum ColorRules {
		ACTION(Color.ORANGE), PLACE(Color.GREEN);
		
		private Color c;
		
		private ColorRules(Color c) {
			this.c = c;
		}
		
		/*
		 * return the color
		 */
		public Color getColor() {
			return this.c;
		}
	}

}
