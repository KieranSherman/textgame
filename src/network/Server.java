package network;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static int portNumber = 4444;

	public static void main(String [] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(portNumber);
		Socket clientSocket = serverSocket.accept();
			
		System.out.println("connection from "+clientSocket.getInetAddress());
		
		serverSocket.close();
	}
	
}
