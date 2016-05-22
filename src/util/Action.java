package util;

/**
 * Abstract class models an action.
 * 
 * @author kieransherman
 *
 */
public abstract class Action {
	
	/**
	 * Executed before main thread.
	 */
	public void pre() {}
	
	/**
	 * Executed after main thread.
	 */
	public void post() {}
	
	/**
	 * Main thread execution.
	 */
	public abstract void execute();
}
