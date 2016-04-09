package main;

import java.awt.Color;

import util.out.Colorer.ColorRules;

/*
 * Class models a word, which can be assigned a color
 */
public class Word {
	
	private String word;
	private ColorRules cr;
	
	public Word(String str, ColorRules cr) {
		this.word = str;
		this.cr = cr;
	}
	
	/*
	 * return the word
	 */
	public String getWord() {
		return word;
	}
	
	/*
	 * returns whether two word object are equivalent
	 */
	public boolean equals(String str) {
		return word.equalsIgnoreCase(str);
	}
	
	/*
	 * returns the color rule
	 */
	public ColorRules getColorRule() {
		return cr;
	}
	
	/*
	 * returns the color of the word
	 */
	public Color getColor() {
		return cr.getColor();
	}

}
