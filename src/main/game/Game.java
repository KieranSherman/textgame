package main.game;

import main.game.multiplayer.MultiplayerGame;
import main.game.singleplayer.SingleplayerGame;
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
	public static void play(int gameMode) {
		if (gameMode == 0)
			SingleplayerGame.play();
		if (gameMode == 1)
			MultiplayerGame.play();
	}
	
	public static synchronized void sendInput(String input) {
		if(input == null) {
			System.err.println("error constructing input");
			return;
		}
		
		if(SingleplayerGame.isRunning() || MultiplayerGame.isRunning())
			GameParse.parseInput(input);
	}
}
