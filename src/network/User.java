package network;

/**
 * Class models a user.
 * 
 * @author kieransherman
 *
 */
public class User {

	private final String hostAddress;
	private final String username;
	
	/**
	 * Create a user at a hostAddress with a username.
	 * 
	 * @param hostAddress the address of the user.
	 * @param username the username.
	 */
	public User(String hostAddress, String username) {
		this.hostAddress = hostAddress;
		this.username = username;
	}
	
	/**
	 * Returns the address the user is connected from.
	 * 
	 * @return the address of the user.
	 */
	public String getHostAddress() {
		return hostAddress;
	}
	
	/**
	 * Return the user's username.
	 * 
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}
	
}
