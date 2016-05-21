package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;

import main.ui.components.Developer;
import main.ui.components.display.DisplayUI;
import main.ui.components.handlers.WindowHandler;
import main.ui.components.input.InputUI;
import main.ui.components.misc.PopupUI;
import main.ui.components.notifications.NotificationUI;
import sound.SoundPlayer;
import util.Action;
import util.Resources;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window {	
	
	private static JFrame windowFrame;		//JFrame container
	private static JPanel windowPanel;
	
	public static JTextPane terminal;		//Pane to display output
	public static JTextPane notes;			//Pane to display notes
	public static JTextField input;			//Field for input
	
	public static DefaultStyledDocument doc;	//*
	public static StyleContext context;			//* Styled for coloring output
	public static Style style;					//*
	
	private Window() {}
	
	public static void initialize(String[] args) {
		createWindow();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				windowFrame.setVisible(true);
				
				Action welcome = new Action() {
					public void execute() {
						PopupUI.displayMessage("WELCOME "+System.getProperty("user.name").toUpperCase());
						
						for(String command : args)
							Developer.parseCommand(command);
					}
				};
				
				Action load = new Action() {
					public void pre() {
						SoundPlayer.play("tapeInsert");
						SoundPlayer.play("computerBeep1");
					}
					public void execute() {
						DisplayUI.initialize();
						Window.input.setEnabled(true);
						Window.input.requestFocus();
					}
					public void post() {
						welcome.execute();
						NotificationUI.queueNotification("LOGIN FINISHING", 500, null, false);
					}
				};
				
				NotificationUI.queueNotification("AUTHORIZING", 500, load, false);
			}
		});
	}

	
	/*
	 * Initializes all components of the Window
	 */
	private static void createWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				windowFrame = new JFrame(Resources.VERSION);
				windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowFrame.addWindowListener(new WindowHandler());
				windowFrame.setResizable(false);
				
				windowPanel = new JPanel();
				windowPanel.setBackground(new Color(15, 15, 15));
				windowPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
				windowPanel.setLayout(new BorderLayout());
				
				windowPanel.add(DisplayUI.createDisplay(), BorderLayout.CENTER);
				windowPanel.add(InputUI.createInput(), BorderLayout.SOUTH);
				
				windowFrame.add(windowPanel);
				windowFrame.pack();
				windowFrame.setSize(Resources.WIDTH, Resources.HEIGHT);
				windowFrame.setLocationByPlatform(true);
				windowFrame.setLocationRelativeTo(null);
				windowFrame.setAlwaysOnTop(true);
				
				SoundPlayer.play("computerStartup");
				SoundPlayer.loop("computerHum");
				SoundPlayer.loop("computerHardDrive");
				SoundPlayer.loop("clock");
			}
		});
	}
	
	public static JFrame getFrame() {
		return windowFrame;
	}
	
}