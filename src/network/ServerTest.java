package network;

public class ServerTest {
	
	public static void main(String [] args) {
		Thread serverThread = new Thread(new Server());
		serverThread.start();
	}
	
}
