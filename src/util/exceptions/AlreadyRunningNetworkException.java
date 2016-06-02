package util.exceptions;

/**
 * Class extends {@link Exception}. 
 * 
 * @author kieransherman
 */
public class AlreadyRunningNetworkException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AlreadyRunningNetworkException(String message) {
		super(message);
	}

}
