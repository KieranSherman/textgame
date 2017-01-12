package main.game.shared;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	public static List<Object> items = new ArrayList<Object>();
	
	int numConnections;
	
	boolean generated = false;
	
	public Room(int connections) {
		numConnections = connections;
	}
}