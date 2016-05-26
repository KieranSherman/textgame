package main.game.multiplayer;

import java.util.ArrayList;
import java.util.List;

import main.game.shared.Player;
import main.game.shared.World;
import util.out.DefaultLogger;

public class MultiplayerGame {
	
	static boolean initialized = false;
	
	static List<Player> players = new ArrayList<Player>();
	
	public static void play() {
		initialize();
		begin();
	}
	
	public static void initialize() {
		initialized = true;
		World.initialize();
		players.add(new Player());
	}
	
	public static void begin() {
		DefaultLogger.appendText("Welcome child. Please allow the darkness to enter your soul.");
	}
	
	public static boolean isRunning() {
		return initialized;
	}
}