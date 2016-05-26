package main.game.singleplayer;

import main.game.shared.Player;
import main.game.shared.World;
import util.out.DefaultLogger;

public class SingleplayerGame {
	
	static boolean initialized = false;
	
	static Player player;
	
	public static void play() {
		initialize();
		begin();
	}
	
	public static void initialize() {
		initialized = true;
		World.initialize();
		player = new Player();
	}
	
	public static void begin() {
		DefaultLogger.appendText("Welcome child. Please allow the darkness to enter your soul.");
	}
	
	public static boolean isRunning() {
		return initialized;
	}
}