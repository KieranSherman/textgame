package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
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
import network.Adapter;
import network.packet.Packet;
import network.packet.types.PacketTypes;
import sound.SoundPlayer;
import util.Action;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window extends JPanel {	
	
	private static final long serialVersionUID = 1L;
	
	private static JFrame window;				//JFrame container
	
	public static JTextPane terminal;		//Pane to display output
	public static JTextPane notes;
	public static JTextField input;		//Field for input
	
	public static DefaultStyledDocument doc;	//*
	public static StyleContext context;		//* Styled for coloring output
	public static Style style;				//*
	
	public static Colorer colorer;			//Parser determines coloring
	public static Adapter adapter;			//Network adapter
	
	static {
		try {
			adapter = Resources.getAdapter();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			colorer = Resources.getColorer();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Window() {
		this.init();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
				
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
						DisplayUI.boot();
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
	private void init() {
		Window mainPanel = this;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame(Resources.VERSION);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.addWindowListener(new WindowHandler());
				window.setResizable(false);
				
				mainPanel.setBackground(new Color(15, 15, 15));
				mainPanel.setBorder(new LineBorder(new Color(15, 15, 15), 2, true));
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(DisplayUI.createTextPane(), BorderLayout.CENTER);
				mainPanel.add(InputUI.createTextField(), BorderLayout.SOUTH);
				
				window.add(mainPanel);
				window.pack();
				window.setSize(Resources.WIDTH, Resources.HEIGHT);
				window.setLocationByPlatform(true);
				window.setLocationRelativeTo(null);
				window.setAlwaysOnTop(true);

				synchronized(mainPanel) {
					mainPanel.notifyAll();
				}
			}
		});
	}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc()
	 */
	public synchronized static void appendText(String str) {
		for(String word : str.split("\\s+")) {
			Color color = colorer.getColor(word);
			Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
			StyleConstants.setForeground(style, alpha);
			DisplayUI.insertTextToDoc(word+" ");
		}
		
		if(!str.contains("\n"))
			DisplayUI.insertTextToDoc("\n");
	}
	
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
		
		Color color = colorer.getPacketColor(packetType);
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(style, alpha);
		DisplayUI.insertTextToDoc(str+"\n");
		
		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
	}
	
	public static JFrame getFrame() {
		return window;
	}
	
}