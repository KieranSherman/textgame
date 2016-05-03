package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import sound.Sound;
import util.Resources;

public class NotificationPaneUI {
	
	private static JPanel notifications = new JPanel();
	private static ArrayList<Notification> notificationQueue = new ArrayList<Notification>();
	private static int notificationSize;	
	
	public static Component getNotificationPane() {
		notifications.setLayout(new GridLayout(10, 1, 0, 8));
		notifications.setPreferredSize(new Dimension(200, Resources.HEIGHT));
		notifications.setOpaque(false);
		
		return notifications;
	}
	
	public static void addNotification(Object obj, int disposeTime) {
		JLabel label = new JLabel(obj.toString());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(Resources.USER_OUTPUT);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setOpaque(true);
		
		JPanel notification = new JPanel();
		notification.setPreferredSize(new Dimension(Resources.WIDTH/6, 50));
		notification.setBackground(new Color(120, 120, 120, 100));
		notification.setLayout(new BorderLayout());
		notification.add(label, BorderLayout.CENTER);
		notification.add(progressBar, BorderLayout.SOUTH);
		notification.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
		
		insertNotification(new Notification(notification, disposeTime));
	}
	
	private static void insertNotification(Notification notification) {
		if(notificationSize >= 10) {
			notificationQueue.add(notification);
			return;
		}
		
		JPanel panel = notification.getJPanel();
		int disposeTime = notification.getDisposeTime();
		
		notificationSize += 1;
		notifications.add(panel);
		notifications.revalidate();
		notifications.repaint();
		
		Sound.notification.play();

		Thread t = new Thread() {
			public void run() {
				JProgressBar progressBar = (JProgressBar)panel.getComponent(1);
				progressBar.setMaximum(disposeTime/50);
				
				for(int i = 0; i < disposeTime/50; i++) {
					try { Thread.sleep(50); } catch (Exception e) {}
					progressBar.setValue(i);
				}
				
				notificationSize -= 1;
				notifications.remove(panel);
				
				if(!notificationQueue.isEmpty() && notificationSize < 10)
					insertNotification(notificationQueue.remove(0));
				
				notifications.revalidate();
				notifications.repaint();
			}
		};
		
		t.start();
	}

}
