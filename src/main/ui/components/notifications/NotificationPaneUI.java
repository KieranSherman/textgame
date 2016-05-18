package main.ui.components.notifications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.ui.components.backgrounds.NotificationBackground;
import main.ui.components.backgrounds.NotificationPanelBackground;
import sound.Sound;
import util.Resources;

public class NotificationPaneUI {
	
	private static JPanel notifications = new NotificationPanelBackground();
	private static ArrayList<Notification> notificationQueue = new ArrayList<Notification>();
	private static int notificationSize = 0;
	private static int notificationCapacity = 10;
	
	public static Component getNotificationPane() {
		Border linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		Border titledBorder = BorderFactory.createTitledBorder(linedBorder, "NOTIFICATIONS", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), new Color(40, 190, 230, 180));
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, notifications.getBorder());
		
		notifications.setLayout(new GridLayout(notificationCapacity, 1, 0, 8));
		notifications.setPreferredSize(new Dimension(200, Resources.HEIGHT));
		notifications.setBorder(compoundBorder);
		
		return notifications;
	}
	
	public static void addNotification(Object obj, int disposeTime) {
		JTextPane area = new JTextPane();
		area.setEditable(false);
		area.setOpaque(false);
		area.setHighlighter(null);
		area.setMargin(new Insets(7, 0, 0, 0));
		area.setFont(Resources.DOS.deriveFont(12f));
		area.setForeground(new Color(255, 255, 255, 180));
		area.setText(obj.toString());
		
		StyledDocument doc = area.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setOpaque(false);
		progressBar.setFont(Resources.DOS.deriveFont(12f));
		progressBar.setStringPainted(true);
		
		JPanel notification = new NotificationBackground();
		notification.setPreferredSize(new Dimension(Resources.WIDTH/6, 50));
		notification.setLayout(new BorderLayout());
		notification.setBorder(new MatteBorder(0, 0, 2, 0, new Color(40, 190, 230, 180)));
		notification.add(area, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(-8, 0, -2, 0));
		panel.setOpaque(false);
		panel.add(progressBar);
		
		notification.add(panel, BorderLayout.SOUTH);
		
		addNotification(new Notification(notification, disposeTime));
	}
	
	private static void addNotification(Notification notification) {
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
		
		Sound.notification.setGain(-6f);
		Sound.notification.play();
		
		Thread t = new Thread() {
			public void run() {
				JProgressBar progressBar = (JProgressBar)((JPanel)panel.getComponent(1)).getComponent(0);
				progressBar.setMaximum(disposeTime/fps);
				
				for(int i = 0; i < disposeTime/fps; i++) {
					try { Thread.sleep(fps); } catch (Exception e) {}
					progressBar.setValue(i);
					panel.repaint();
				}
				
				notificationSize -= 1;
				notifications.remove(panel);
				
				if(!notificationQueue.isEmpty() && notificationSize < notificationCapacity)
					addNotification(notificationQueue.remove(0));
				
				notifications.revalidate();
				notifications.repaint();
			}
		};
		
		t.start();
	}

}
