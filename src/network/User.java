package network;

public class User {

	private final String hostAddress;
	private final String username;
	
	public User(String hostAddress, String username) {
		this.hostAddress = hostAddress;
		this.username = username;
	}
	
	public String getHostAddress() {
		return hostAddress;
	}
	
	public String getUsername() {
		return username;
	}
	
}
