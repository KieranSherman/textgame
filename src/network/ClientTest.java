package network;

public class ClientTest {
	
	public static void main(String [] args) {
		Thread clientThread = new Thread(new Client());
		clientThread.start();
	}

}
