package main.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Class handles what happens on window events
 */
public class WindowHandler implements WindowListener {
	
	private Window window;
	
	public WindowHandler(Window window) {
		this.window = window;
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	/*
	 * Closes connections when window is closing
	 */
	public void windowClosing(WindowEvent e) {
		window.closeConnections();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
