package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import bridge.ClientBridge;
import bridge.ServerBridge;
import network.Client;
import network.Server;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	private JFrame window;				//JFrame container
	private JTextPane textPane;			//Pane for output
	private JTextField textField;		//Field for input
	
	private DefaultStyledDocument doc;	//*
	private StyleContext context;		//* Styled for coloring output
	private Style style;				//*
	
	private Colorer colorer;			//Parser determines coloring
	
	private Server server;
	private Client client;
	
	private ServerBridge serverB;
	private ClientBridge clientB;
	
	public Window() {
		Resources.init(this);
		this.init();
	}
	
	//POST: members initialized
	private void init() {
		
		JPanel panel = this;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame("");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setResizable(false);
				
				panel.setBackground(new Color(15, 15, 15));
				panel.setLayout(new BorderLayout());
		
				doc = new DefaultStyledDocument();
				textPane = new JTextPane(doc);
				context = new StyleContext();
				style = context.addStyle("TextGame", null);
				
				try {
					colorer = Resources.getColorer();
				} catch (ResourcesNotInitializedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
				textPane.setEditable(false);
				textPane.setFont(Resources.def);
				textPane.setBackground(new Color(15, 15, 15));
				textPane.setForeground(Color.WHITE);
				textPane.setMargin(new Insets(0, 10, 0, 10));
				
				Border lineB = BorderFactory.createLineBorder(Color.WHITE);
				Border b = BorderFactory.createTitledBorder(lineB, "COMMLINK", TitledBorder.CENTER, 
						TitledBorder.TOP, new Font("Dense", Font.BOLD, 15), Color.RED);
				Border compound = BorderFactory.createCompoundBorder(b, textPane.getBorder());
				
				textPane.setBorder(compound);
		
				JScrollPane scroll = new JScrollPane(textPane);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scroll.setBorder(null);
				
				panel.add(scroll, BorderLayout.CENTER);
				
				// - TEXTFIELD - //
				
				textField = new JTextField("out:: ");
				textField.setFont(Resources.def);
				textField.setBackground(new Color(15, 15, 15));
				textField.setForeground(Color.WHITE);
				textField.setCaretColor(Color.WHITE);
				
				b = BorderFactory.createTitledBorder(lineB, "COMMS", TitledBorder.CENTER, 
						TitledBorder.TOP, new Font("Dense", Font.BOLD, 15), Color.GREEN);
				compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
				
				textField.setBorder(compound);
				
				textField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(client != null)
							client.write(textField.getText());
						
						if(server != null)
							server.write(textField.getText());
						
						appendText("> "+textField.getText().substring(6));
						
						setText("out:: ");
					}
				});
				
				textField.addKeyListener(new KeyListener() {
					@Override
					public void keyReleased(KeyEvent e) {
						if(!textField.getText().startsWith("out:: "))
							setText("out:: ");
					}
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {
						int keyCode = e.getKeyCode();
						
						if(keyCode == KeyEvent.VK_BACK_SPACE) {
							if(textField.getText().equals("out:: "))
								setText("out:: ");
						}
					}
				});
				
				panel.add(textField, BorderLayout.SOUTH);
				
				// - WINDOW - //
				
				window.add(panel);
				window.pack();
				window.setSize(Resources.WIDTH, Resources.HEIGHT);
				window.setLocationByPlatform(true);
				window.setLocationRelativeTo(null);
				
				window.setVisible(true);
			}
		});
	}
	
	//PRE: str != null
	//POST: sets the text field text to str
	private void setText(String str) {
		textField.setText(str);
	}
	
	//PRE: str != null
	//POST: colors and appends str to the text pane
	public void appendText(final String str) {
		if(parseCommand(str))
			return;

		String [] split = str.split("\\s+");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for(String s : split) {
						
						StyleConstants.setForeground(style, colorer.getColor(s));
						doc.insertString(doc.getLength(), s+" ", style);
					}
					
					doc.insertString(doc.getLength(), "\n", null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private boolean parseCommand(String str) {
		str = str.substring(2);
		
		if(!str.startsWith("!"))
			return false;
		
		str = str.toLowerCase().substring(1);
		String [] args = str.split("\\s+");
		
		if(args[0].equals("server")) {
			if(args.length == 1)
				server = new Server();
			if(args.length == 2)
				server = new Server(Integer.parseInt(args[1]));
			
			serverB = new ServerBridge(server);
			server.setBridge(serverB);
			
			Thread serverThread = new Thread(server);
			serverThread.start();

			appendText("server initialized");
		}
		
		if(args[0].equals("client")) {
			if(args.length == 1)
				client = new Client();
			else if(args.length == 2)
				client = new Client(args[1]);
			else if (args.length == 3)
				client = new Client(args[1], Integer.parseInt(args[2]));
			
			clientB = new ClientBridge(client);
			client.setBridge(clientB);
			
			Thread clientThread = new Thread(client);
			clientThread.start();
			
			appendText("client initialized");
		}
		
		return true;
	}

}
