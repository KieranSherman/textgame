package main.game.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	
	final static int MAX_CONNECTIONS = 4;
	
	final static long SEED = 1234;
	
	static Random random = new Random();
	
	public static List<Room> rooms = new ArrayList<Room>();

	public static List<List<Integer>> world = new ArrayList<List<Integer>>();
	
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
		addRooms(rooms.get(14));
		addRooms(rooms.get(15));
		addRooms(rooms.get(16));
		addRooms(rooms.get(17));
		addRooms(rooms.get(18));
		addRooms(rooms.get(19));
		addRooms(rooms.get(20));
		addRooms(rooms.get(21));
		addRooms(rooms.get(22));
		addRooms(rooms.get(23));
		addRooms(rooms.get(24));
		addRooms(rooms.get(25));
		addRooms(rooms.get(26));
		addRooms(rooms.get(27));
		addRooms(rooms.get(28));
		addRooms(rooms.get(29));
		addRooms(rooms.get(30));
		addRooms(rooms.get(31));
		addRooms(rooms.get(32));
		addRooms(rooms.get(33));
		addRooms(rooms.get(34));
		addRooms(rooms.get(35));
		addRooms(rooms.get(36));
		addRooms(rooms.get(37));
		addRooms(rooms.get(38));
		addRooms(rooms.get(39));
		addRooms(rooms.get(40));
		print();
	}
	
	public static void addFirstRooms() {
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
	}
	
	public static void addRooms(Room current) {
		int newRooms = current.numConnections - numConnections(current);
		if(newRooms == 0)
			return;
		for(int i = 0; i < newRooms; i++) {
			addRoom(current);
		}
	}
	
	public static void addRoom(Room current) {
		int connect1 = random.nextInt(MAX_CONNECTIONS) + 1;
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
		
		if(connect1 >= 3 && random.nextInt(2) == 1) {
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
	}
	
	public static int numConnections(Room r) {
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
}