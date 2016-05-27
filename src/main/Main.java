package main;

import java.awt.Color;

import javax.swing.UIManager;

import main.game.Game;
import network.Adapter;
import network.upnp.UPnPGateway;
import sound.SoundPlayer;

/*
 * Main method creates a game and plays it.
 */
public class Main {
	
	private Main() {}
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UIManager.put("ProgressBar.background", new Color(40, 190, 230, 180));
		UIManager.put("ProgressBar.foreground", Color.BLACK);
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
		UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
		
		UIManager.put("ScrollBarUI", "main.ui.components.display.scrollbars.ScrollBarUI_Vertical");
		UIManager.put("ScrollBarUI", "main.ui.components.display.scrollbars.ScrollBarUI_Horizontal");
		
		Runtime.getRuntime().addShutdownHook(new Thread("ShutdownThread") {
			public void run() {
				Main.shutdown();
			}
		});
		
		Main.start(args);		
	}
	
	public static void restart(String[] args) {
		Adapter.close();
		Main.start(args);
	}
	
	private static void start(String[] args) {
		Game.init(args);
		Game.play(0);
	}
	
	private static void shutdown() {
		SoundPlayer.setMuted(true);
		
		UPnPGateway.disconnect();
		System.out.println("UPnP shutdown successfully");
		
		Adapter.close();
		System.out.println("Adapter shutdown successfully");
	}
	
}
