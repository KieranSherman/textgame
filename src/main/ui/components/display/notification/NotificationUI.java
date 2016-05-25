package main.ui.components.display.notification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.ui.components.display.background.PanelBackground;
import sound.SoundPlayer;
import util.Action;
import util.Resources;

/**
 * This class consists exclusively of static methods that affect the notification display.
 * 
 * @author kieransherman
 * @see #createNotificationDisplay(JPanel, String)
 *
 */
public class NotificationUI {
	
	private volatile static JPanel notifications, background;
	private volatile static ArrayList<Notification> notificationQueue;
	private volatile static int notificationSize, notificationCapacity;
	
	static {
		notificationQueue = new ArrayList<Notification>();
		notificationSize = 0;
		notificationCapacity = 10;
	}
	
	// Prevent object instantiation
	private NotificationUI() {}
	
	/**
	 * Adds a notification panel to the specified panel at a {@link BorderLayout} position.
	 * 
	 * @param addToPanel the panel to add the notification panel to.
	 * @param borderLayout the {@link BorderLayout} position.
	 */
	public static void createNotificationDisplay(JPanel addToPanel, String borderLayout) {
		notifications = new JPanel(new GridLayout(notificationCapacity, 1, 0, 8));
		notifications.setOpaque(false);
		notifications.setPreferredSize(new Dimension(200, Resources.WINDOW_HEIGHT));
		notifications.setBorder(Resources.getBorder("TODO", new Color(40, 190, 230, 180)));
		
		background = new PanelBackground(Resources.notesBG);
		background.setLayout(new BorderLayout());
		background.add(notifications);
		
		addToPanel.add(background, borderLayout);
	}
	
	/**
	 * Creates and queues a notification.  If the current display is not full, it is immediately removed
	 * from the queue and executed.  Otherwise, it remains in the queue until the current
	 * display is no longer full.
	 * 
	 * @param label the notification's label.
	 * @param disposeTime the dispose time of the notification.
	 * @param action the action to execute after disposed.
	 * @param playSound whether or not to play a notification sound.
	 */
	public static void queueNotification(String label, int disposeTime, Action action, boolean playSound) {
		JPanel notification = new PanelBackground(Resources.notesBG);

		JTextPane area = new JTextPane();
		area.setEditable(false);
		area.setOpaque(false);
		area.setHighlighter(null);
		area.setMargin(new Insets(25, 0, 0, 0));
		area.setFont(Resources.DOS.deriveFont(12f));
		area.setForeground(new Color(255, 255, 255, 180));
		
		if(label.toString().length() > 20) {
			area.setText(label.toString().substring(0, 18)+"...");
			notification.setToolTipText(label.toString());
		} else {
			area.setText(label.toString());
		}
		
		StyledDocument doc = area.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		JProgressBar pB = new JProgressBar();
		pB.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		pB.setFont(Resources.DOS.deriveFont(12f));
		pB.setStringPainted(true);
		
		notification.setPreferredSize(new Dimension(Resources.WINDOW_WIDTH/6, 50));
		notification.setLayout(new BorderLayout());
		notification.add(area, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(pB);
		
		notification.add(panel, BorderLayout.SOUTH);
		
		addNotification(new Notification(notification, disposeTime, action), playSound);
	}
	
	/**
	 * Checks to see if the current notification display is full.  If so, the notification is queued.
	 * Otherwise, the notification is executed on its own thread immediately.
	 */
	private static void addNotification(Notification notification, boolean playSound) {
		if(notificationSize >= notificationCapacity) {
			notificationQueue.add(notification);
			return;
		}
		
		JPanel panel = notification.getJPanel();
		int disposeTime = notification.getDisposeTime();
		int fps = 25;
		
		notificationSize += 1;
		notifications.add(panel);
		notifications.revalidate();
		notifications.repaint();
		
		if(playSound)
			SoundPlayer.play("notification");
		
		Thread t = new Thread("NotificationThread-"+notificationSize) {
			public void run() {
				JProgressBar progressBar = (JProgressBar)((JPanel)panel.getComponent(1)).getComponent(0);
				progressBar.setMaximum(disposeTime/fps);
				
				for(int i = 0; i < disposeTime/fps; i++) {
					try { Thread.sleep(fps); } catch (Exception e) {}
					progressBar.setValue(i);
					panel.repaint();
				}
				
				notification.execute();
				
				notificationSize -= 1;
				notifications.remove(panel);
				
				if(!notificationQueue.isEmpty() && notificationSize < notificationCapacity)
					addNotification(notificationQueue.remove(0), true);
				
				notifications.revalidate();
				notifications.repaint();
			}
		};
		t.start();
	}

	/**
	 * Returns the {@link JPanel} of the {@link NotificationUI}.
	 * 
	 * @return the notification JPanel.
	 */
	public static JPanel getPanel() {
		return background;
	}
	
}
