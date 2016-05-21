package main.ui.components.notifications;

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

import main.ui.components.backgrounds.PanelBackground;
import main.ui.components.status.StatusUI;
import sound.SoundPlayer;
import util.Action;
import util.Resources;

public class NotificationUI {
	
	private volatile static JPanel notifications, mainPanel;
	private volatile static ArrayList<Notification> notificationQueue;
	private volatile static int notificationSize, notificationCapacity;
	
	static {
		notificationQueue = new ArrayList<Notification>();
		notificationSize = 0;
		notificationCapacity = 10;
	}
	
	private NotificationUI() {}
	
	public static JPanel createNotificationDisplay() {
		notifications = new JPanel(new GridLayout(notificationCapacity, 1, 0, 8));
		notifications.setOpaque(false);
		notifications.setPreferredSize(new Dimension(200, Resources.HEIGHT));
		notifications.setBorder(Resources.getBorder("TODO", new Color(40, 190, 230, 180)));
		
		mainPanel = new PanelBackground(Resources.notesBG);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(notifications, BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	public static void createStatusDisplay() {
		if(mainPanel.getComponentCount() > 1)
			return;
		
		((GridLayout)notifications.getLayout()).setRows((notificationCapacity = 6));
		
		mainPanel.add(StatusUI.createStatusPane(), BorderLayout.SOUTH);
		mainPanel.revalidate();
	}
	
	public static void removeStatusDisplay() {
		if(mainPanel.getComponentCount() < 2)
			return;
		
		((GridLayout)notifications.getLayout()).setRows((notificationCapacity = 10));
		
		mainPanel.remove(1);
		mainPanel.revalidate();
	}
	
	public static void queueNotification(Object obj, int disposeTime, Action action, boolean playSound) {
		JPanel notification = new PanelBackground(Resources.notesBG);

		JTextPane area = new JTextPane();
		area.setEditable(false);
		area.setOpaque(false);
		area.setHighlighter(null);
		area.setMargin(new Insets(25, 0, 0, 0));
		area.setFont(Resources.DOS.deriveFont(12f));
		area.setForeground(new Color(255, 255, 255, 180));
		
		if(obj.toString().length() > 20) {
			area.setText(obj.toString().substring(0, 18)+"...");
			notification.setToolTipText(obj.toString());
		} else {
			area.setText(obj.toString());
		}
		
		StyledDocument doc = area.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		JProgressBar pB = new JProgressBar();
		pB.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		pB.setFont(Resources.DOS.deriveFont(12f));
		pB.setStringPainted(true);
		
		notification.setPreferredSize(new Dimension(Resources.WIDTH/6, 50));
		notification.setLayout(new BorderLayout());
		notification.add(area, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(pB);
		
		notification.add(panel, BorderLayout.SOUTH);
		
		addNotification(new Notification(notification, disposeTime, action), playSound);
	}
	
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
		
		new Thread("NotificationThread-"+notificationSize) {
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
		}.start();
	}

}
