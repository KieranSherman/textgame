package network.server;

public class ServerModifier {
	
	private ServerModifier() {}

	public static void setLocalHostMaximum(int localHostMaximum) {
		Server.localHostMaximum = localHostMaximum; 
	}
	
	public static void setSameClientMaximum(int sameClientMaximum) {
		Server.sameClientMaximum = sameClientMaximum;
	}
	
	public static void setClientConnectionMaximum(int clientConnectionMaximum) {
		Server.clientConnectionMaximum = clientConnectionMaximum;
	}
	
}
