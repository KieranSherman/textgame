package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.ui.components.display.DisplayUI;
import main.ui.components.handlers.WindowHandler;
import main.ui.components.input.InputUI;
import main.ui.components.misc.PopupUI;
import main.ui.components.notifications.NotificationPaneUI;
import network.Adapter;
import network.packet.Packet;
import network.packet.types.Packet03Message;
import network.packet.types.Packet04Action;
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
	
	public Window() {
		this.init();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
				
				Action welcome = new Action() {
					public void execute() {
						PopupUI.displayMessage("WELCOME "+System.getProperty("user.name").toUpperCase());
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
						
						NotificationPaneUI.queueNotification("LOGIN FINISHING", 1000, null, false);
						NotificationPaneUI.queueNotification("LOADING RESOURCES", 800, null, false);
						NotificationPaneUI.queueNotification("LOADING TEXTURES", 900, null, false);
						NotificationPaneUI.queueNotification("LOADING AMBIENCE", 700, null, false);
						NotificationPaneUI.queueNotification("GENERATING CHEESE", 800, null, false);
						NotificationPaneUI.queueNotification("AQUIRING GPS", 800, null, false);
						NotificationPaneUI.queueNotification("LOSING SANITY", 900, null, false);
						NotificationPaneUI.queueNotification("EATING PIE", 700, null, false);
					}
				};
				NotificationPaneUI.queueNotification("AUTHORIZING", 500, load, false);
			}
		});
	}
	
	/*
	 * Initializes all components of the Window
	 */
	private void init() {
		Window mainPanel = this;
		
		try {
			colorer = Resources.getColorer();
		} catch (ResourcesNotInitializedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					adapter = Resources.getAdapter();
				} catch (ResourcesNotInitializedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
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
				
				AbstractAction command = new AbstractAction() {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						PopupUI.getInput("{ ENTER COMMAND }");
						String command = PopupUI.getData();
						Window.input.setText("");
						Window.parseCommand(command);
					}
				};
				
				input.getInputMap().put(KeyStroke.getKeyStroke('`'), "EnterCommand");
				input.getActionMap().put("EnterCommand", command);
				input.setEnabled(false);
				
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
	
	/*
	 * Checks to see if str is a command and executes
	 * the necessary action
	 */
	public static void parseCommand(String str) {
		if(str == null)
			return;
		
		String [] args = str.split("\\s+");
		
		if(args[0].equals("server")) {
			String port = "9999";
			
			for(String s : args)
				if(s.contains("p:"))
					port = s.substring(s.indexOf(":")+1);
			
			adapter.createServer(Integer.parseInt(port));
			window.setTitle(Resources.VERSION+" | running server");
		}
		else
		if(args[0].equals("client")) {
			String username = "anonymous";
			String address = "localhost";
			String port = "9999";
			
			for(String s : args)
				if(s.contains("u:"))
					username = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("a:"))
					address = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("p:"))
					port = s.substring(s.indexOf(":")+1);
						
			adapter.createClient(address, Integer.parseInt(port), username);
			window.setTitle(Resources.VERSION+" | running client");
		}
		else
		if(args[0].equals("block")) {
			if(args.length == 1)
				adapter.block(false);
			else if(args.length == 2)
				adapter.block(Boolean.parseBoolean(args[1]));
		}
		else
		if(args[0].equals("clear")) {
			DisplayUI.clear();
		}
		else
		if(args[0].equals("popup")) {
			PopupUI.getInput("POPUP TEST");
		}
		else
		if(args[0].equals("notify")) {
			String message = "NOTIFICATION TEST";
			int time = 2000;
			int repeat = 1;
			boolean random = false;
			
			for(String s : args)
				if(s.contains("m:"))
					message = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("t:"))
					time = Integer.parseInt(s.substring(s.indexOf(":")+1));
			
			for(String s : args)
				if(s.contains("x"))
					repeat = Integer.parseInt(s.substring(s.indexOf("x")+1));
			
			for(String s : args)
				if(s.contains("r:"))
					random = Boolean.parseBoolean(s.substring(s.indexOf(":")+1));
			
			for(int i = 0; i < repeat; i++)
				if(random)
					NotificationPaneUI.queueNotification(message.toUpperCase()+" "+(i+1), (int)(time+Math.random()*10000), null, true);
				else
					NotificationPaneUI.queueNotification(message.toUpperCase()+" "+(i+1), time, null, true);
		}
		else
		if(args[0].equals("status")) {
			adapter.status();
		}
		else
		if(args[0].equals("logout")) {
			adapter.close();
			window.setTitle(Resources.VERSION);
		}
		else
		if(args[0].equals("notes")) {
			adapter.sendPacket(new Packet03Message("START >>>>>>>\n"+notes.getText()+"\n<<<<<<< END"));
			appendColoredText("[sent notes]", Color.GRAY);
		}
		else {
			adapter.sendPacket(new Packet04Action(str));
			appendText("! "+str);
		}
	}
	
	public static void setTitle(String str) {
		window.setTitle(str);
	}
	
	public static JFrame getFrame() {
		return window;
	}
	
}