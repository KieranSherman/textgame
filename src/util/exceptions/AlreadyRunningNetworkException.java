package util.exceptions;

public class AlreadyRunningNetworkException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AlreadyRunningNetworkException() {
		super();
	}
	
	public AlreadyRunningNetworkException(String message) {
		super(message);
	}

}
