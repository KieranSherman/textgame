package main;

import main.ui.Window;
import sound.Sound;

/*
 * Class models a game
 */
public class Game {
		
	@SuppressWarnings("unused")
	private Window window;
	
	public Game() {
		window = new Window();
		Sound.background1.loop();
	}
	
	public void play() {
		
	}

}
