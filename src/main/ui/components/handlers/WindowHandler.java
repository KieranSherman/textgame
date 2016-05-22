package main.ui.components.handlers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.ui.Window;
import network.Adapter;
import network.upnp.UPnPGateway;

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
		UPnPGateway.disconnect();
		Adapter.close();
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
		Window.input.requestFocus();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
