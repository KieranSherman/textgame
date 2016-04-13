package main.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Class handles what happens on window events
 */
public class WindowHandler implements WindowListener {
	
	public WindowHandler() {}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	/*
	 * closes connections when window is closing
	 */
	public void windowClosing(WindowEvent e) {
		Window.adapter.close();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	/*
	 * request textField focus upon window activation
	 */
	public void windowActivated(WindowEvent e) {
		Window.textField.requestFocus();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
