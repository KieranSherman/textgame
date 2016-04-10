package util.exceptions;

/*
 * Exception is thrown if Resources is not initialized when user
 * attempts to access various members
 */
public class ResourcesNotInitializedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ResourcesNotInitializedException() {
		super();
	}
	
	public ResourcesNotInitializedException(String message) {
		super(message);
	}
	
}
