package main.ui.components;

import javax.swing.JPanel;

public class Notification {
	
	private JPanel panel;
	private int disposeTime;
	
	public Notification(JPanel panel, int disposeTime) {
		this.panel = panel;
		this.disposeTime = disposeTime;
	}
	
	public JPanel getJPanel() {
		return panel;
	}
	
	public int getDisposeTime() {
		return disposeTime;
	}

}
