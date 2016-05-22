package util;

import java.awt.Color;

import util.out.Colorer.ColorRules;

/**
 * Class models a word.
 * 
 * @author kieransherman
 *
 */
public class Word {
	
	private String word;
	private ColorRules cr;
	
	public Word(String str, ColorRules cr) {
		this.word = str;
		this.cr = cr;
	}
	
	/**
	 * Returns the word as a {@code String}.
	 * 
	 * @return the word {@code String}.
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns whether a word equals a {@code String}.
	 * 
	 * @param str the {@code String} to test.
	 * @return whether the word and {@code String} are the same.
	 */
	public boolean equals(String str) {
		return word.equalsIgnoreCase(str);
	}
	
	
	/**
	 * Returns the word's coloring rule.
	 * 
	 * @return the color rule.
	 */
	public ColorRules getColorRule() {
		return cr;
	}
	
	/**
	 * Return the word's color.
	 * 
	 * @return the color.
	 */
	public Color getColor() {
		return cr.getColor();
	}

}
