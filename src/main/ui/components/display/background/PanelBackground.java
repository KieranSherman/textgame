package main.ui.components.display.background;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import util.Resources;

/**
 * This class extends {@link JPanel}, overriding its {@link #paintComponent()} method.  Upon intantiation,
 * it sets the background of the {@link JPanel} to an image passed through the constructor, and starts
 * a new custom-rendering thread, updating every {@link Resources#RENDER_SPEED} milliseconds.
 * 
 * @author kieransherman
 *
 */
public class PanelBackground extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image imageBackground;
	private Thread displayThread;
	private JPanel panel;
	
	/**
	 * Creates a new {@link JPanel}, and overrides its {@code paintComponent(Graphics g)} method to paint
	 * an image as the background.
	 * 
	 * @param imageBackground the background image.
	 */
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(imageBackground, 0, 0, Resources.WIDTH, Resources.HEIGHT, null);
	}

}
