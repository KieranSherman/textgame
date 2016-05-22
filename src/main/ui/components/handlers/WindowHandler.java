package main.ui.components.handlers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.ui.Window;
import network.Adapter;
import network.upnp.UPnPGateway;

/**
 * Class extends {@link WindowListener} to modify window closing events.
 * 
 * @author kieransherman
 */
public class WindowHandler implements WindowListener {
	
	public WindowHandler() {}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	/**
	 * Removes the UPnP gateway and closes the adapter.
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
	public void windowActivated(WindowEvent e) {
		Window.input.requestFocus();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
