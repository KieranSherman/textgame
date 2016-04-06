package util;

import java.awt.Color;
import java.util.ArrayList;

import main.Word;

public class Colorer {
	
	private ArrayList<Word> words;
	
	public Colorer() {
		words = new ArrayList<Word>();
		
		words.add(new Word("move", ColorRules.ACTION));
	}
	
	public void addWord(Word word) {
		words.add(word);
	}
	
	public void addWord(String word, ColorRules cr) {
		words.add(new Word(word, cr));
	}
	
	public Color getColor(String str) {
		for(Word word : words)
			if(word.equals(str))
				return word.getColor();
		
		return Color.WHITE;
	}
	
	public enum ColorRules {
		ACTION(Color.RED), PLACE(Color.GREEN);
		
		private Color c;
		
		private ColorRules(Color c) {
			this.c = c;
		}
		
		public Color getColor() {
			return this.c;
		}
	}

}
