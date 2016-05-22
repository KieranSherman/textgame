package main.ui.components.display.notification;

import javax.swing.JPanel;

import sound.SoundPlayer;
import util.Action;

public class Notification {
	
	private JPanel panel;
	private int disposeTime;
	private Action action;
	
	public Notification(JPanel panel, int disposeTime, Action action) {
		this.panel = panel;
		this.disposeTime = disposeTime;
		this.action = action;
	}
	
	public JPanel getJPanel() {
		return panel;
	}
	
	public int getDisposeTime() {
		return disposeTime;
	}
	
	public void execute() {
		if(action != null) {
			action.pre();
			action.execute();
			action.post();
		}
		
		SoundPlayer.play("tapeEject");
	}

}
