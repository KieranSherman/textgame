package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import network.Adapter;
import network.packet.PacketTypes;
import network.packet.types.Packet03Text;
import util.Resources;
import util.exceptions.ResourcesNotInitializedException;
import util.out.Colorer;
import util.out.Formatter;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	private JFrame window;				//JFrame container
	
	public JTextPane textPane;			//Pane to display output
	public JTextField textField;		//Field for input
	
	private DefaultStyledDocument doc;	//*
	private StyleContext context;		//* Styled for coloring output
	private Style style;				//*
	
	private Colorer colorer;			//Parser determines coloring
	
	private Adapter adapter;			//Network adapter
	
	public Window() {
		Resources.init(this);
		this.init();
	}
	
	/*
	 * Initializes all components of the Window
	 */
	private void init() {
		Window panel = this;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("ScrollBarUI", "main.ui.ScrollBarUI");
				try {
					adapter = Resources.getAdapter();
				} catch (ResourcesNotInitializedException e) {
					e.printStackTrace();
					System.exit(1);
				}

				window = new JFrame("");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setResizable(false);
				window.addWindowListener(new WindowHandler(panel));
				
				panel.setBackground(new Color(15, 15, 15));
				panel.setLayout(new BorderLayout());
		
				init_textPane();
				init_textField();
				
				window.add(panel);
				window.pack();
				window.setSize(Resources.WIDTH, Resources.HEIGHT);
				window.setLocationByPlatform(true);
				window.setLocationRelativeTo(null);
				window.setAlwaysOnTop(true);
				
				textField.requestFocus();
				
				window.setVisible(true);
			}
		});
	}
	
	/*
	 * Initializes the text pane
	 */
	private void init_textPane() {
		Window panel = this;
		panel.setBorder(new LineBorder(Color.BLACK, 2, true));

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
		textPane.setFont(Resources.USER_OUTPUT);
		textPane.setBackground(new Color(15, 15, 15));
		textPane.setForeground(Color.WHITE);
		textPane.setMargin(new Insets(0, 10, 0, 10));
		textPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField.requestFocus();
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		Border lineB = BorderFactory.createLineBorder(Color.WHITE);
		Border b = BorderFactory.createTitledBorder(lineB, "COMMLINK", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.UI, Resources.DARK_RED);
		Border compound = BorderFactory.createCompoundBorder(b, textPane.getBorder());
		
		JScrollPane scroll = new JScrollPane(textPane);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.getVerticalScrollBar().setUI(new ScrollBarUI());
		scroll.setBackground(new Color(15, 15, 15));
		scroll.setBorder(compound);
		
		panel.add(scroll, BorderLayout.CENTER);
	}
	
	/*
	 * Initializes the text field
	 */
	private void init_textField() {
		JPanel panel = this;
		
		JPanel inputField = new JPanel();
		inputField.setLayout(new BorderLayout());
		inputField.setBackground(new Color(15, 15, 15));
		inputField.setForeground(Color.WHITE);
		
		JLabel promptText = new JLabel("out:: ");
		promptText.setForeground(Color.WHITE);
		promptText.setFont(Resources.USER_OUTPUT);
		inputField.add(promptText, BorderLayout.WEST);
		
		textField = new JTextField();
		textField.setFont(Resources.USER_OUTPUT);
		textField.setBackground(new Color(15, 15, 15));
		textField.setForeground(Color.WHITE);
		textField.setCaretColor(Color.WHITE);
		textField.setBorder(null);
		inputField.add(textField, BorderLayout.CENTER);
		
		Border lineB = BorderFactory.createLineBorder(Color.WHITE);
		Border b = BorderFactory.createTitledBorder(lineB, "COMMS", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.UI, Resources.DARK_GREEN);
		Border compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
		
		inputField.setBorder(compound);
		
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = textField.getText();

				appendText("> "+str);
				setText("");
				
				if(!isCommand(str) && str != null)
					adapter.sendPacket(new Packet03Text(str));
			}
		});
		
		panel.add(inputField, BorderLayout.SOUTH);
	}
	
	/*
	 * Sets the text of textField to str
	 */
	private void setText(String str) {
		textField.setText(str);
	}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc()
	 */
	public void appendText(final String toAppend) {
		if(toAppend == null || parseCommand(toAppend) || parsePacket(toAppend))
			return;
	
		for(String str : toAppend.split("\\s+")) {
			StyleConstants.setForeground(style, colorer.getColor(str));
			insertTextToDoc(str+" ");
		}
		
		insertTextToDoc("\n");
	}
	
	/*
	 * Inserts text into the styled doc
	 */
	private void insertTextToDoc(String str) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					doc.insertString(doc.getLength(), str, style);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Checks to see if str is a command and executes
	 * the necessary action
	 */
	private boolean parseCommand(String str) {
		if(str == null)
			return false;
		
		str = str.substring(2);
		
		if(!isCommand(str))
			return false;
		
		str = str.toLowerCase().substring(1);
		String [] args = str.split("\\s+");
		
		if(args[0].equals("server")) {
			if(args.length == 1)
				adapter.createServer();
			if(args.length == 2)
				adapter.createServer(Integer.parseInt(args[1]));
			
			adapter.startServer();
			window.setTitle("running server");
		}
		
		if(args[0].equals("client")) {
			if(args.length == 1)
				adapter.createClient();
			else if(args.length == 2)
				adapter.createClient(args[1]);
			else if (args.length == 3)
				adapter.createClient(args[1], Integer.parseInt(args[2]));
			
			adapter.startClient();
			window.setTitle("running client");
		}
		
		if(args[0].equals("logout")) {
			adapter.close();
		}
		
		return true;
	}
	
	/*
	 * Checks to see if str is from a packet
	 */
	private boolean parsePacket(String str) {
		int offset = 0;
		
		if(str.startsWith(Formatter.getFormat(PacketTypes.LOGIN))) {
			StyleConstants.setForeground(style, Color.CYAN);
			offset = Formatter.getFormat(PacketTypes.LOGIN).length();
		} else 
		if(str.startsWith(Formatter.getFormat(PacketTypes.DISCONNECT))) {
			StyleConstants.setForeground(style, Color.GRAY);
			offset = Formatter.getFormat(PacketTypes.DISCONNECT).length();
		} else 
		if(str.startsWith(Formatter.getFormat(PacketTypes.TEXT))) {
			StyleConstants.setForeground(style, Color.MAGENTA);
			offset = Formatter.getFormat(PacketTypes.TEXT).length();
		} else {
			return false;
		}
		
		insertTextToDoc(str.substring(offset)+"\n");
		
		return true;
	}
	
	/*
	 * Returns whether or not the String is a command
	 */
	private boolean isCommand(String str) {
		return str.startsWith("!");
	}
	
	/*
	 * Closes the connection from the adapter
	 */
	public void closeConnections() {
		adapter.close();
	}

}
