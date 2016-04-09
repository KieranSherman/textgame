package util.out;

import java.awt.Color;
import java.util.ArrayList;

import main.Word;

/*
 * Class holds Words and determines their color 
 */
public class Colorer {
	
	private ArrayList<Word> words;		//words
	
	public Colorer() {
		words = new ArrayList<Word>();
	}
	
	/*
	 * Add a word to the list
	 */
	public void addWord(Word word) {
		words.add(word);
	}
	
	/*
	 * Add a word to the list with a specific ColorRule
	 */
	public void addWord(String word, ColorRules cr) {
		words.add(new Word(word, cr));
	}
	
	/*
	 * Return the color of the word
	 */
	public Color getColor(String str) {
		for(Word word : words)
			if(word.equals(str))
				return word.getColor();
		
		return Color.WHITE;
	}
	
	/*
	 * Coloring rules for different types of words
	 */
	public enum ColorRules {
		ACTION(Color.RED), PLACE(Color.GREEN);
		
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
