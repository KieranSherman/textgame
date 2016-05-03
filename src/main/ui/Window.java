package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.BootThread;
import main.ui.components.NotificationPaneUI;
import main.ui.components.PopUpPanelUI;
import main.ui.components.TextFieldInputUI;
import main.ui.components.TextPaneDisplayUI;
import network.Adapter;
import network.packet.Packet;
import network.packet.types.Packet03Message;
import network.packet.types.Packet04Action;
import network.packet.types.PacketTypes;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window extends JPanel {	
	
	private static final long serialVersionUID = 1L;
	
	private static JFrame window;				//JFrame container
	
	protected static JTextPane textPane;		//Pane to display output
	protected static JTextPane notes;
	protected static JTextField textField;		//Field for input
	
	protected static DefaultStyledDocument doc;	//*
	protected static StyleContext context;		//* Styled for coloring output
	protected static Style style;				//*
	
	protected static Colorer colorer;				//Parser determines coloring
	protected static Adapter adapter;				//Network adapter
	
	private BootThread bootThread;				//Thread controlling boot
	
	public Window() {
		boot();
		
		Resources.init(this);
		this.init();

		synchronized(this) {
			try { this.wait(); } catch (InterruptedException e) {}
		}
		
		synchronized(this) {
			BootThread.startWindow(this);
			try { this.wait(); } catch (InterruptedException e) {}
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
			}
		});
		
		if(bootThread != null)
			bootThread.close();
	}
	
	/*
	 * Handles boot
	 */
	private void boot() {
		if(!Resources.boot)
			return;
		
		bootThread = new BootThread();
		Resources.sleep(400);
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
				UIManager.put("ScrollBarUI", "main.ui.components.ScrollBarUI_Vertical");
				UIManager.put("ScrollBarUI", "main.ui.components.ScrollBarUI_Horizontal");

				try {
					adapter = Resources.getAdapter();
				} catch (ResourcesNotInitializedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
				window = new JFrame(Resources.VERSION);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.addWindowListener(new WindowHandler());
				
				mainPanel.setBackground(new Color(15, 15, 15));
				mainPanel.setBorder(new LineBorder(new Color(15, 15, 15), 2, true));
				mainPanel.setLayout(new BorderLayout());
		
				mainPanel.add(TextPaneDisplayUI.createTextPane(), BorderLayout.CENTER);
				BootThread.queueInfo("textPane loaded");
				
				mainPanel.add(TextFieldInputUI.createTextField(), BorderLayout.SOUTH);
				BootThread.queueInfo("textField loaded");
				
				window.add(mainPanel);
				window.pack();
				window.setSize(Resources.WIDTH, Resources.HEIGHT);
				window.setLocationByPlatform(true);
				window.setLocationRelativeTo(null);
				window.setAlwaysOnTop(true);
				
				textField.requestFocus();
				
				BootThread.queueInfo("window initialized");
				
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
		if(parseCommand(str))
			return;
	
		for(String word : str.split("\\s+")) {
			StyleConstants.setForeground(style, colorer.getColor(word));
			TextPaneDisplayUI.insertTextToDoc(word+" ");
		}
		
		TextPaneDisplayUI.insertTextToDoc("\n");
	}
	
	public synchronized static void appendColoredText(String str, Color color) {
		StyleConstants.setForeground(style, color);
		TextPaneDisplayUI.insertTextToDoc(str+"\n");
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
		
		StyleConstants.setForeground(style, colorer.getPacketColor(packetType));
		TextPaneDisplayUI.insertTextToDoc(str+"\n");
	}	
	
	/*
	 * Checks to see if str is a command and executes
	 * the necessary action
	 */
	private static boolean parseCommand(String str) {
		if(str == null || !(str = str.substring(2)).startsWith("!"))
			return false;
		
		str = str.toLowerCase().substring(1);
		String [] args = str.split("\\s+");
		
		if(args.length == 0 || args == null)
			return true;
		else
		if(args[0].equals("server")) {
			String port = "9999";
			
			for(String s : args)
				if(s.contains("p:"))
					port = s.substring(s.indexOf(":")+1);
			
			adapter.createServer(Integer.parseInt(port));
			adapter.startServer();
			
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
			adapter.startClient();
			
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
			textPane.setText("");
		}
		else
		if(args[0].equals("popup")) {
			new PopUpPanelUI(window, "Hello!");
		}
		else
		if(args[0].equals("notify")) {
			String message = "notification test";
			int time = 2000;
			
			for(String s : args)
				if(s.contains("m:"))
					message = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("t:"))
					time = Integer.parseInt(s.substring(s.indexOf(":")+1));
			
			NotificationPaneUI.addNotification(message.toUpperCase(), time);
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
			appendText("> "+str);
		}
		
		return true;
	}
	
	public static void setTitle(String str) {
		window.setTitle(str);
	}
	
}