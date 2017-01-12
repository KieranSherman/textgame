package main.game.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	
	private static int MAX_CONNECTIONS = 4;
	
	private static long SEED = 0;
	
	private static Random random = new Random();
	
	private static List<Room> rooms = new ArrayList<Room>();

	private static List<List<Integer>> world = new ArrayList<List<Integer>>();

	public static void initialize() {
		generateSeed();
		random.setSeed(SEED);
		addFirstRooms();
		print();
	}
	
	private static void generateSeed() {
		Random rand = new Random();
		SEED = rand.nextLong();
	}
	
	private static void addFirstRooms() {
		int connect = random.nextInt(MAX_CONNECTIONS) + 1;
		Room room = new Room(connect);
		
		rooms.add(room);
		List<Integer> c = new ArrayList<Integer>();
		
		c.add(0);
		world.add(c);
		for(int i = 1; i <= connect; i++) {
			int connect1 = random.nextInt(MAX_CONNECTIONS) + 1;
			Room r = new Room(connect1);
			rooms.add(r);
			List<Integer> c1 = new ArrayList<Integer>();
			c1.add(5-i);
			for(int j = 0; j < room.numConnections; j++)
				c1.add(0);
			world.add(c1);
			
			c.add(i);
		}
		room.generated = true;
	}
	
	public static void addRoom(Room current) {
		int connect1 = random.nextInt(MAX_CONNECTIONS) + 1;
		System.out.println(connect1);
		Room r = new Room(connect1);
		rooms.add(r);
		for (List<Integer> c : world) {
		    c.add(0);
		}
		List<Integer> c = new ArrayList<Integer>();
		
		for(int i = 0; i < rooms.size(); i++)
			c.add(0);
		
		world.add(c);
		
		int num = 0;
		boolean one = false, two = false, three = false, four = false;
		for(int i = 0; i < rooms.size(); i++) {
			int n = world.get(i).get(rooms.indexOf(current));
			if(n == 1)
				one = true;
			if(n == 2)
				two = true;
			if(n == 3)
				three = true;
			if(n == 4)
				four = true;
		}
		
		if(one && two && three && four)
			num = 0;
		if(!four) {
			num = 4;
			four = true;
		}
		if(!three) {
			num = 3;
			three = true;
		}
		if(!two) {
			num = 2;
			two = true;
		}
		if(!one) {
			num = 1;
			one = true;
		}
		
		c.set(rooms.indexOf(current), num);
		world.get(rooms.indexOf(current)).set(rooms.indexOf(r), 5-(num == 0 ? 5 : num));
		
		if(connect1 >= 3 && random.nextInt(3) == 1) {
			int back = random.nextInt(rooms.size()-2) + 1;
			int search = 0;
			boolean skip = false;
			while(rooms.get(back).numConnections - numConnections(rooms.get(back)) == 0){
				back = random.nextInt(rooms.size()-2) + 1;
				search++;
				if (search == 101) {
					skip = true;
					break;
				}
			}
			
			if (!skip) {
				one = false; two = false; three = false; four = false;
				for(int i = 0; i < rooms.size(); i++) {
					int n = world.get(i).get(back);
					if(n == 1)
						one = true;
					if(n == 2)
						two = true;
					if(n == 3)
						three = true;
					if(n == 4)
						four = true;
				}
				
				for(int i = 0; i < rooms.size(); i++) {
					int n = world.get(i).get(rooms.indexOf(current));
					if(n == 1)
						one = true;
					if(n == 2)
						two = true;
					if(n == 3)
						three = true;
					if(n == 4)
						four = true;
				}
			
				if(one && two && three && four)
					num = 0;
				if(!four)
					num = 4;
				if(!three)
					num = 3;
				if(!two)
					num = 2;
				if(!one)
					num = 1;
				
				if (num != 0) {
					c.set(back, num);
					world.get(back).set(rooms.indexOf(r), 5-num);
				}
			}
		}
		current.generated = true;
	}
	
	private static int numConnections(Room r) {
		int num = 0;
		for(int b : world.get(rooms.indexOf(r)))
			if (b != 0)
			num ++;
		return num;
	}
	
	public static void print() {
		for(int i = 0; i < rooms.size(); i++) {
			for(int j = 0; j < rooms.size(); j++) {
				System.out.print(world.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}

	public static int getRoom(int current, int direction) {
		int r = world.get(current).indexOf(direction);
		if(r == -1)
			return r;
		if(rooms.get(r).generated == false)
			addRoom(rooms.get(r));
		return r;
	}
	
	public static boolean roomExists(int current, int direction) {
		int r = world.get(current).indexOf(direction);
		if(r == -1)
			return false;
		return true;
	}
	
	public static int getRoomConnections(int r) {
		return rooms.get(r).numConnections;
	}
}