package main.ui.components.display.notification;

import javax.swing.JPanel;

import sound.SoundPlayer;
import util.Action;

/**
 * This class models a notification.  Objects of this class will execute an {@link Action} after a given
 * dispose time has passed, which will start after queued by the {@link NotificationUI}.
 * 
 * @author kieransherman
 * @see NotificationUI
 * @see Action
 *
 */
public class Notification {
	
	private JPanel panel;
	private int disposeTime;
	private Action action;
	
	/**
	 * Creates a new {@link Notification} object.  The {@link NotificationUI} will determine when the
	 * object is queued and displayed.
	 * 
	 * @param panel			the panel to display
	 * @param disposeTime	the time before the panel is disposed
	 * @param action		the action to execute after the panel is disposed
	 */
	public Notification(JPanel panel, int disposeTime, Action action) {
		this.panel = panel;
		this.disposeTime = disposeTime;
		this.action = action;
	}
	
	/**
	 * Returns the {@link Notification} object's {@link JPanel}.
	 * 
	 * @return the {@link JPanel} object of the notification.
	 */
	public JPanel getJPanel() {
		return panel;
	}
	
	/**
	 * Returns the {@link Notification} object's dispose time.
	 * 
	 * @return the dispose time of the notification.
	 */
	public int getDisposeTime() {
		return disposeTime;
	}
	
	/**
	 * Executes the {@link Action} of the {@link Notification} object.  Execution follows the order of
	 * {@code pre(), execute(), post()}.
	 */
	public void execute() {
		if(action != null) {
			action.pre();
			action.execute();
			action.post();
		}
		
		SoundPlayer.play("tapeEject");
	}

}
