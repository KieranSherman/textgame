package main.misc;

import main.ui.Window;
import sound.SoundPlayer;

/**
 * This class models a game.
 * @author kieransherman
 * 
 */
public class Game {
	
	private Game() {}
	
	/**
	 * Initializes the game, executing args as startup commands.
	 * @param args the startup commands
	 */
	public static void init(String[] args) {
		Window.initialize(args);
		SoundPlayer.loop("background1");
	}
	
	/**
	 * Starts the game.
	 */
	public static void play() {}

}
