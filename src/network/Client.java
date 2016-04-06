package network;

import java.net.Socket;

public class Client {
	
	private static String hostName = "localhost";
	private static int portNumber = 4444;
	
	public static void main(String [] args) throws Exception {
		Socket socket = new Socket(hostName, portNumber);
		
		System.out.println("connection to "+socket.getInetAddress());
	
		socket.close();
	}

}
