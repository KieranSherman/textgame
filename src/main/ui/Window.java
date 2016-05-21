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
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.ui.components.Developer;
import main.ui.components.display.DisplayUI;
import main.ui.components.handlers.WindowHandler;
import main.ui.components.input.InputUI;
import main.ui.components.misc.PopupUI;
import main.ui.components.notifications.NotificationUI;
import network.packet.Packet;
import network.packet.types.PacketTypes;
import sound.SoundPlayer;
import util.Action;
import util.Resources;
import util.out.Colorer;

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
	
	public static void initialize() {
		createWindow();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				windowFrame.setVisible(true);
				
				Action welcome = new Action() {
					public void execute() {
						PopupUI.displayMessage("WELCOME "+System.getProperty("user.name").toUpperCase());
						Developer.parseCommand("dev kletus");
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
						
						NotificationUI.queueNotification("LOGIN FINISHING", 1000, null, false);
						NotificationUI.queueNotification("LOADING RESOURCES", 800, null, false);
						NotificationUI.queueNotification("LOADING TEXTURES", 900, null, false);
						NotificationUI.queueNotification("LOADING AMBIENCE", 700, null, false);
						NotificationUI.queueNotification("GENERATING CHEESE", 800, null, false);
						NotificationUI.queueNotification("AQUIRING GPS", 800, null, false);
						NotificationUI.queueNotification("LOSING SANITY", 900, null, false);
						NotificationUI.queueNotification("EATING PIE", 700, null, false);
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
			}
		});
	}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc()
	 */
	public synchronized static void appendText(String str) {
		for(String word : str.split("\\s+")) {
			Color color = Colorer.getColor(word);
			Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
			StyleConstants.setForeground(style, alpha);
			DisplayUI.insertTextToDoc(word+" ");
		}
		
		if(!str.contains("\n"))
			DisplayUI.insertTextToDoc("\n");
	}
	
	/*
	 * Appends str to the end of textPane with set color;
	 * acts as filter to method: insertTextToDoc();
	 */
	public synchronized static void appendColoredText(String str, Color color) {
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(style, alpha);
		DisplayUI.insertTextToDoc(str+"\n");
	}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc();
	 * exclusively for the PacketParser
	 */
	public synchronized static void appendPacket(Packet packet) {
		PacketTypes packetType = packet.getType();
		String str = (String)packet.getData();
		
		if(packetType == PacketTypes.ACTION) {
			appendText(str);
			return;
		}
		
		Color color = Colorer.getPacketColor(packetType);
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(style, alpha);
		DisplayUI.insertTextToDoc(str+"\n");
		
		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
	}
	
	public static JFrame getFrame() {
		return windowFrame;
	}
	
}