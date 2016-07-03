package main.game.shared;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	public static List<Object> items = new ArrayList<Object>();
	
	int numConnections;
	
	public Room(int connections) {
		numConnections = connections;
	}

	public Room getNorth() {
		return this;
	}

	public Room getEast() {
		return this;
	}

	public Room getSouth() {
		return this;
	}

	public Room getWest() {
		return this;
	}
	
}