package main.misc;

import main.ui.Window;
import sound.SoundPlayer;

/*
 * Class models a game
 */
public class Game {
	
	private Game() {}
	
	public static void init() {
		Window.initialize();
		SoundPlayer.loop("background1");
	}
	
	public static void play() {}

}
