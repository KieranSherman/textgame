package main.game.shared;

import java.awt.Color;

import util.out.DefaultLogger;

public class Player {
	
	public int currentRoom;
	
	public Player() {
		setRoom(0);
	}
	
	public Player(int room) {
		setRoom(room);
	}

	public void setRoom(int room) {
		currentRoom = room;
	}
	
	public void travel(String direction) {
		switch(direction) {
			case "north" :
				int r = World.getRoom(currentRoom, 1);
				if(r == -1)
					DefaultLogger.appendColoredText("You cannot travel north!", Color.RED);
				else {
					setRoom(r);
					changedRooms();
				}
				break;
			case "east" :
				r = World.getRoom(currentRoom, 2);
				if(r == -1)
					DefaultLogger.appendColoredText("You cannot travel east!", Color.RED);
				else {
					setRoom(r);
					changedRooms();
				}
				break;
			case "south" :
				r = World.getRoom(currentRoom, 4);
				if(r == -1)
					DefaultLogger.appendColoredText("You cannot travel south!", Color.RED);
				else {
					setRoom(r);
					changedRooms();
				}
				break;
			case "west" :
				r = World.getRoom(currentRoom, 3);
				if(r == -1)
					DefaultLogger.appendColoredText("You cannot travel west!", Color.RED);
				else {
					setRoom(r);
					changedRooms();
				}
				break;
		}
	}
	
	public void changedRooms() {
		String north = "", east = "", south = "", west = "";
		if(World.roomExists(currentRoom, 1))
			north = " [north]";
		if(World.roomExists(currentRoom, 2))
			east = " [east]";
		if(World.roomExists(currentRoom, 4))
			south = " [south]";
		if(World.roomExists(currentRoom, 3))
			west = " [west]";
		DefaultLogger.appendColoredText("You are standing in a room with doors to your:" + north + east + south + west, new Color(177, 123, 247));
		DefaultLogger.appendColoredText("[room number " + currentRoom + "]", Color.CYAN);
	}
}