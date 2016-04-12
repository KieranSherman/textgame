package main.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jmx.remote.internal.ArrayQueue;

import util.Resources;

public class BootThread extends Thread {
	
	private static JFrame window;
	private static String info = "";
	
	private static ArrayQueue<String> queue;
	
	public BootThread() {
		queue = new ArrayQueue<String>(50);
		new Thread(this).start();
	}
	
	public void run() {
		window = new JFrame();
		window.setSize(Resources.WIDTH, Resources.HEIGHT);
		window.setUndecorated(true);
		window.setLocationRelativeTo(null);
		window.setAlwaysOnTop(true);
		
		window.add(new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(final Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(Resources.bootImage, 0, 0, Resources.WIDTH, Resources.HEIGHT, null);
				g.setColor(Color.WHITE);
				g.setFont(Resources.USER_OUTPUT);
				g.drawString(Resources.VERSION, 20, Resources.HEIGHT-20);
				g.drawString(info, 75, Resources.HEIGHT-20);
			}
		});
		
		window.setVisible(true);
	}
	
	public static void queueInfo(String info) {
		if(queue == null)
			return;
		
		queue.add(info);
		
		new Thread() {
			public void run() {
				try { Thread.sleep(queue.size()*50);} catch (Exception e) {}
				BootThread.info = queue.remove(0)+"...";
				window.repaint();
			}
		}.start();
	}
	
	public static void startWindow(Window window) {
		new Thread() {
			public void run() {
				while(queue != null && !queue.isEmpty())
					try { Thread.sleep(500); } catch (Exception e) {}
				
				synchronized(window) {
					window.notifyAll();
				}
			}
		}.start();
	}
	
	public void close() {
		window.dispose();
	}
	
}
