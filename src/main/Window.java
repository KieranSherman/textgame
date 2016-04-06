package main;

import java.awt.BorderLayout;
import java.awt.Color;
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

import util.Colorer;
import util.Resources;

/*
 * Class models a window with an exterior JFrame and interior JPanel
 */
public class Window extends JPanel {	
	private static final long serialVersionUID = 1L;
	
	private JFrame window;		//JFrame container
	private JTextPane textPane;
	private JTextField textField;
	private DefaultStyledDocument doc;
	private StyleContext context;
	private Style style;
	private Colorer colorer;
	
	public Window() {
		Resources.init();
		
		init();
	}
	
	//POST: members initialized
	private void init() {
		window = new JFrame("");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		this.setBackground(new Color(15, 15, 15));
		this.setLayout(new BorderLayout());

		doc = new DefaultStyledDocument();
		textPane = new JTextPane(doc);
		context = new StyleContext();
		style = context.addStyle("TextGame", null);
		colorer = Resources.colorer;
		
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
		
		this.add(scroll, BorderLayout.CENTER);
		
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
		
		this.add(textField, BorderLayout.SOUTH);
		
		// - WINDOW - //
		
		window.add(this);
		window.pack();
		window.setSize(Resources.WIDTH, Resources.HEIGHT);
		window.setLocationByPlatform(true);
		window.setLocationRelativeTo(null);
		
		window.setVisible(true);
	}
	
	public void setText(String str) {
		textField.setText(str);
	}
	
	public void appendText(String str) {
		String [] split = str.split("\\s+");
		
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

}
