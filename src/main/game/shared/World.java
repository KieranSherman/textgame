package main.game.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	
	final static int NUM_ROOMS = 162;
	final static int MAX_CONNECTIONS = 4;
	
	final static long SEED = 1234;
	
	static Random random = new Random();
	
	public static List<Room> rooms = new ArrayList<Room>();

	public static List<List<Boolean>> world = new ArrayList<List<Boolean>>();
	
	public static void main(String[] args) {
		initialize();
	}

	public static void initialize() {
		//random.setSeed(SEED);
		addFirstRooms();
		addRooms(rooms.get(1));
		addRooms(rooms.get(2));
		addRooms(rooms.get(3));
		addRooms(rooms.get(4));
		addRooms(rooms.get(5));
		addRooms(rooms.get(6));
		addRooms(rooms.get(7));
		addRooms(rooms.get(8));
		addRooms(rooms.get(9));
		addRooms(rooms.get(10));
		addRooms(rooms.get(11));
		addRooms(rooms.get(12));
		addRooms(rooms.get(13));
		print();
	}
	
	public static void addFirstRooms() {
		Room r = new Room();
		rooms.add(r);
		List<Boolean> c = new ArrayList<Boolean>();
		c.add(false);
		world.add(c);
		addRooms(r);
	}
	
	public static void addRooms(Room current) {
		int cu = numConnections(current);
		int connect = 0;
		if (cu != MAX_CONNECTIONS)
			connect = random.nextInt(MAX_CONNECTIONS - cu) + 1;
		for(int i = 0; i < connect; i++) {
			Room r = new Room();
			addRoom(r, current);
		}
	}
	
	public static void addRoom(Room r, Room current) {
		rooms.add(r);
		for (List<Boolean> c : world) {
		    c.add(false);
		}
		List<Boolean> c = new ArrayList<Boolean>();
		
		for(int i = 0; i < rooms.size(); i++)
			c.add(false);
		
		c.set(rooms.indexOf(current), true);
		world.get(rooms.indexOf(current)).set(rooms.indexOf(r), true);
		world.add(c);
		int connect = random.nextInt(MAX_CONNECTIONS);
		for(int i = 0; i < connect; i++) {
			int n = random.nextInt(rooms.size()-1);
			if(numConnections(rooms.get(n)) < MAX_CONNECTIONS) {
				c.set(n, true);
				world.get(n).set(rooms.indexOf(r), true);
			}
		}
	}
	
	public static int numConnections(Room r) {
		int num = 0;
		for(Boolean b : world.get(rooms.indexOf(r)))
			num += b ? 1 : 0;
		return num;
	}
	
	public static void print() {
		for(int i = 0; i < rooms.size(); i++) {
			for(int j = 0; j < world.get(i).size(); j++) {
				System.out.print((world.get(i).get(j) ? 1 : 0) + " ");
			}
			System.out.println();
		}
	}
}