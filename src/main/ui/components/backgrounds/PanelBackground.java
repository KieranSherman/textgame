package main.ui.components.backgrounds;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import util.Resources;

public class PanelBackground extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Image imageBackground;
	private Thread displayThread;
	private JPanel panel;
	
	public PanelBackground(Image imageBackground) {
		this.imageBackground = imageBackground;
		
		panel = this;
		
		displayThread = new Thread() {
			public void run() {
				while(true) {
					panel.repaint();
					try { Thread.sleep(Resources.RENDER_SPEED); } catch (Exception e) {}
				}
			}
		};
		
		displayThread.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(imageBackground, 0, 0, Resources.WIDTH, Resources.HEIGHT, null);
	}

}
