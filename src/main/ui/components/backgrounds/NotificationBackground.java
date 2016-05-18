package main.ui.components.backgrounds;

import java.awt.Graphics;

import javax.swing.JPanel;

import util.Resources;

public class NotificationBackground extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public NotificationBackground() {
		JPanel panel = this;
		
		Thread displayThread = new Thread() {
			public void run() {
				while(true) {
					panel.repaint();
					try { Thread.sleep(Resources.RENDER); } catch (Exception e) {}
				}
			}
		};
		
		displayThread.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(Resources.notesBG, 0, 0, Resources.WIDTH, Resources.HEIGHT, null);
	}
	
}
