package main;

import java.awt.Color;

import main.Colorer.ColorRules;

public class Word {
	
	private String word;
	private ColorRules cr;
	
	public Word(String str, ColorRules cr) {
		this.word = str;
		this.cr = cr;
	}
	
	public String getWord() {
		return word;
	}
	
	public boolean equals(String str) {
		return word.equalsIgnoreCase(str);
	}
	
	public ColorRules getColorRule() {
		return cr;
	}
	
	public Color getColor() {
		return cr.getColor();
	}

}
