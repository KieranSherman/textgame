package util.exceptions;

public class ResourcesNotInitializedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ResourcesNotInitializedException() {
		super();
	}
	
	public ResourcesNotInitializedException(String message) {
		super(message);
	}
	
}
