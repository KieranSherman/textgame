package main.misc;

import main.ui.Window;
import sound.SoundPlayer;

/*
 * Class models a game
 */
public class Game {
		
	@SuppressWarnings("unused")
	private Window window;
	
	public Game() {
		window = new Window();
		SoundPlayer.loop("background1");
	}
	
	public void play() {
		
	}

}
