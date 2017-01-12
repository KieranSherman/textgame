package main.game.singleplayer;

import java.awt.Color;

import main.game.shared.Player;
import main.game.shared.World;
import util.out.DefaultLogger;

public class SingleplayerGame {
	
	static boolean initialized = false;
	
	public static Player player;
	
	public static void play() {
		initialize();
		begin();
	}
	
	public static void initialize() {
		initialized = true;
		World.initialize();
		DefaultLogger.appendColoredText("[world initialized]", Color.CYAN);
		player = new Player();
		DefaultLogger.appendColoredText("[player initialized]", Color.CYAN);
	}
	
	public static void begin() {
		player.changedRooms();
	}
	
	public static boolean isRunning() {
		return initialized;
	}
}