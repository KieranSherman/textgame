package main;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.game.Game;
import network.Adapter;

/*
 * Main method creates a game and plays it.
 */
public class Main {
	
	private Main() {}
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		UIManager.put("ProgressBar.background", new Color(40, 190, 230, 180));
		UIManager.put("ProgressBar.foreground", Color.BLACK);
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
		UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
		
		UIManager.put("ScrollBarUI", "main.ui.components.display.scrollbars.ScrollBarUI_Vertical");
		UIManager.put("ScrollBarUI", "main.ui.components.display.scrollbars.ScrollBarUI_Horizontal");
		
		start(args);		
	}
	
	public static void restart(String[] args) {
		Adapter.close();
		start(args);
	}
	
	private static void start(String[] args) {
		Game.init(args);
		Game.play(0);
	}
	
}
