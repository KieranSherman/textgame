package main.game.shared;

public class Player {
	
	Room currentRoom;
	
	public Player() {
		initialize(World.rooms.get(0));
	}
	
	public Player(Room room) {
		initialize(room);
	}
	
	public void initialize(Room room) {
		currentRoom = room;
	}
	
	public void setRoom(Room room) {
		currentRoom = room;
	}
	
	public void travel(String direction) {
		switch(direction) {
			case "north" :
				setRoom(currentRoom.getNorth());
			case "east" :
				setRoom(currentRoom.getEast());
			case "south" :
				setRoom(currentRoom.getSouth());
			case "west" :
				setRoom(currentRoom.getWest());
		}
	}
}