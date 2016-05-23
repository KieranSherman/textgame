package main.ui.components.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import main.Developer;
import main.ui.Window;
import main.ui.components.display.scrollbars.ScrollBarUI_Vertical;
import main.ui.components.input.AutoComplete;
import main.ui.components.input.JTextFieldLimit;
import sound.SoundPlayer;
import util.Resources;

/**
 * This class consists exclusively of static methods that display various types of popups.
 * 
 * @author kieransherman
 * @see #displayMessage(String)
 * @see #promptInput(String, boolean)
 * @see #promptChoice(String, String[])
 * @see #getData()
 *
 */
public class PopupUI {
	
	// Prevent object instantiation
	private PopupUI() {}
	
	private static Object[] data;
	private static JFrame frame = Window.getFrame();
	private static Color panelBG = new Color(10, 10, 10);
	private static Color panelFG = new Color(220, 220, 220);
	private static boolean popupOpen = false;
	
	/**
	 * Displays a message.
	 * 
	 * @param message the message to display.
	 */
	public static void displayMessage(String message) {
		popupOpen = true;
		JDialog dialog = new JDialog(frame, "child", true);
		
		dialog.setUndecorated(true);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
		dialog.setLayout(new BorderLayout());
		dialog.setSize(Resources.WIDTH/5, Resources.HEIGHT/8);
		dialog.setLocationRelativeTo(frame);
		dialog.setOpacity(.9f);
		dialog.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
					popupOpen = false;
				}
			}
		});
		
	    JButton button = new JButton("CLOSE");
	    button.setFont(Resources.DOS);
	    button.setBackground(Color.WHITE);
	    button.setForeground(Color.BLACK);
	    button.setFocusable(false);
	    button.setBorderPainted(false);
	    button.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		dialog.dispose();
	    		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				popupOpen = false;
	    	}
	    });
	    
	    JLabel label = new JLabel(message);
	    label.setForeground(panelFG);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setVerticalAlignment(JLabel.CENTER);
	    label.setFont(Resources.DOS.deriveFont(15f));
	    
	    JPanel panel = new JPanel();
	    panel.setBackground(panelBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		panel.add(button, BorderLayout.SOUTH);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	/**
	 * Promts user for input.  Response stored in data accessed by {@link #getData()}.
	 * 
	 * @param prompt the prompt.
	 * @param useAutoComplete whether or not to use autocomplete.
	 */
	public static void promptInput(String prompt, boolean useAutoComplete) {
		popupOpen = true;
	    SoundPlayer.play("computerBeep2");

		JDialog dialog = new JDialog(frame, "child", true);
		
		dialog.setUndecorated(true);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
		dialog.setLayout(new BorderLayout());
		dialog.setSize(Resources.WIDTH/5, Resources.HEIGHT/8);
		dialog.setLocationRelativeTo(frame);
		dialog.setOpacity(.9f);
		
	    JLabel label = new JLabel(prompt);
	    label.setOpaque(false);
	    label.setBorder(new EmptyBorder(20, 0, 20, 0));
	    label.setForeground(panelFG);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setVerticalAlignment(JLabel.CENTER);
	    label.setFont(Resources.DOS.deriveFont(15f));
	    
	    JPanel inputPanel = new JPanel();
	    inputPanel.setLayout(new BorderLayout());
	    inputPanel.setBackground(Color.BLACK);
	    inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE));
	    
	    JTextField textField = new JTextField();
	    textField.setOpaque(false);
		textField.setFocusTraversalKeysEnabled(false);
	    textField.setCaretColor(Color.WHITE);
	    textField.setFont(Resources.DOS.deriveFont(14f));
	    textField.setForeground(Color.WHITE);
	    textField.setSelectionColor(Color.GRAY);
	    textField.setBorder(null);
	    textField.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		data = new Object[] {textField.getText()};
	    		dialog.dispose();
	    		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				popupOpen = false;
	    	}
	    });
	    
	    final AutoComplete autoComplete = new AutoComplete(textField);
	    if(useAutoComplete) {
	    	if(Developer.isDeveloperModeEnabled())
	    		autoComplete.setKeywordList(Resources.MASTER_COMMANDLIST);
	    	
			textField.getDocument().addDocumentListener(autoComplete);
			textField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "commit");
			textField.getActionMap().put("commit", autoComplete.new CommitAction());
	    }
	    
	    textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					data = null;
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
					popupOpen = false;
				}
				else
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(autoComplete != null && autoComplete.isActive())
						textField.setCaretPosition(textField.getText().length());
				}
			}

		});
	    inputPanel.add(textField, BorderLayout.CENTER);

    	
		
	    JLabel in = new JLabel(" >> ");
	    in.setOpaque(false);
	    in.setFont(Resources.DOS.deriveFont(16f));
	    in.setForeground(Color.WHITE);
	    in.setHorizontalAlignment(JLabel.CENTER);
	    in.setVerticalAlignment(JLabel.CENTER);
	    inputPanel.add(in, BorderLayout.WEST);
	   
	    JPanel panel = new JPanel();
	    panel.setBackground(panelBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(inputPanel, BorderLayout.CENTER);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	/**
	 * Prompts user to make a choice.
	 * 
	 * @param prompt the prompt.
	 * @param choices the array of choices.
	 */
	public static void promptChoice(String prompt, String[] choices) {
		popupOpen = true;
		SoundPlayer.play("computerBeep2");

		JDialog dialog = new JDialog(frame, "child", true);
		dialog.setUndecorated(true);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
		dialog.setLayout(new BorderLayout());
		dialog.setSize(Resources.WIDTH/5, Resources.HEIGHT/8);
		dialog.setLocationRelativeTo(frame);
		dialog.setOpacity(.9f);
		
	    JLabel promptLabel = new JLabel(prompt);
	    promptLabel.setOpaque(false);
	    promptLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
	    promptLabel.setForeground(panelFG);
	    promptLabel.setHorizontalAlignment(JLabel.CENTER);
	    promptLabel.setVerticalAlignment(JLabel.CENTER);
	    promptLabel.setFont(Resources.DOS.deriveFont(15f));
	    
	    JButton[] buttons = new JButton[choices.length];
		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(choices[i]);
			buttons[i].setFont(Resources.DOS.deriveFont(16f));
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setBackground(Color.BLACK);
			buttons[i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
			buttons[i].setFocusable(false);
			
			String choice = choices[i];
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
					data = new Object[] {choice};
					popupOpen = false;
				}
			});
		}
	    
	    JPanel buttonPanel = new JPanel(new GridLayout(1, choices.length, 0, 0));
	    buttonPanel.setBackground(Color.BLACK);
	    buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE));
	    for(int i = 0; i < buttons.length; i++)
	    	buttonPanel.add(buttons[i]);
	   
	    JPanel panel = new JPanel();
	    panel.setBackground(panelBG);
		panel.setLayout(new BorderLayout());
		panel.add(promptLabel, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.CENTER);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	/**
	 * Promts the user to write a report.
	 * 
	 * @param prompt the prompt.
	 * @param title the title's starting text.
	 * @param description the description's starting text.
	 * @param maxTitleChars the maximum number title characters.
	 */
	public static void promptReport(String prompt, String title, String description, int maxTitleChars) {
		popupOpen = true;
		SoundPlayer.play("computerBeep2");

		JDialog dialog = new JDialog(frame, "child", true);
		dialog.setUndecorated(true);
		dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
		dialog.setLayout(new BorderLayout());
		dialog.setSize(250, 250);
		dialog.setLocationRelativeTo(frame);
		dialog.setOpacity(.9f);
		
	    JLabel label = new JLabel(prompt);
	    label.setBorder(new EmptyBorder(10, 0, 10, 0));
	    label.setBackground(panelBG);
	    label.setForeground(panelFG);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setVerticalAlignment(JLabel.CENTER);
	    label.setFont(Resources.DOS.deriveFont(16f));
	    
	    JTextField textField = new JTextField();
	    textField.setOpaque(false);
	    textField.setDocument(new JTextFieldLimit(maxTitleChars));
	    textField.setText(title);
	    textField.selectAll();
	    textField.setHorizontalAlignment(JTextField.CENTER);
	    textField.setForeground(new Color(255, 50, 50, 180));
	    textField.setSelectionColor(Color.GRAY);
	    textField.setMargin(new Insets(10, 10, 10, 10));
	    textField.setFont(Resources.DOS.deriveFont(16f));
	    textField.setBorder(new EmptyBorder(10, 10, 10, 10));
	    textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					data = null;
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
					popupOpen = false;
				}
			}
		});
	    
	    JTextArea textArea = new JTextArea(description);
	    textArea.selectAll();
	    textArea.setLineWrap(true);
	    textArea.setWrapStyleWord(true);
	    textArea.setCaretColor(Color.WHITE);
	    textArea.setFont(Resources.DOS.deriveFont(14f));
	    textArea.setForeground(Color.WHITE);
	    textArea.setBackground(new Color(20, 20, 20));
	    textArea.setSelectionColor(Color.GRAY);
	    textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
	    textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					data = null;
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
					popupOpen = false;
				}
			}
		});
	    textArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "doNothing");
	    textArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "doNothing");
	    textArea.getActionMap().put("doNothing", null);
	    
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    scrollPane.getViewport().setOpaque(false);
	    scrollPane.setOpaque(false);
	    scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
		scrollPane.getVerticalScrollBar().setOpaque(false);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBar(null);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.setBorder(null);
	    
	    JComboBox<String> version = new JComboBox<String>();
	    for(String str : Resources.ALL_VERSIONS)
	    	version.addItem(str);
	    version.setFont(Resources.DOS);
	    version.setBackground(Color.LIGHT_GRAY);
	    version.setForeground(Color.BLACK);
	    version.setFocusable(false);
	    version.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
	    version.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
	    
	    JButton submit_button = new JButton("SUBMIT");
	    submit_button.setFont(Resources.DOS);
	    submit_button.setBackground(Color.WHITE);
	    submit_button.setForeground(Color.BLACK);
	    submit_button.setFocusable(false);
	    submit_button.setBorderPainted(false);
	    submit_button.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		data = new Object[] {textField.getText(), textArea.getText(), version.getSelectedItem()};
	    		dialog.dispose();
	    		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				popupOpen = false;
	    	}
	    });
	    
	    JPanel inputPanel = new JPanel();
	    inputPanel.setLayout(new BorderLayout());
	    inputPanel.setOpaque(false);
	    inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE));
	    inputPanel.add(textField, BorderLayout.NORTH);
	    inputPanel.add(scrollPane, BorderLayout.CENTER);
	    inputPanel.add(version, BorderLayout.SOUTH);
	    
	    JPanel panel = new JPanel();
	    panel.setBackground(panelBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(inputPanel, BorderLayout.CENTER);
		panel.add(submit_button, BorderLayout.SOUTH);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.setVisible(true);
	}
	
	/**
	 * Returns whether or not a popup is already open.
	 * 
	 * @return whether popup is already open.
	 */
	public static boolean popupAlreadyOpen() {
		return popupOpen;
	}
	
	/**
	 * Returns the data, set by the most recent prompt.
	 * 
	 * @return the most recent data.
	 */
	public static Object[] getData() {
		return data;
	}
	
}