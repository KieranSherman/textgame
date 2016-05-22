package util;

public abstract class Action {
	public void pre() {}
	public void post() {}
	public abstract void execute();
}
