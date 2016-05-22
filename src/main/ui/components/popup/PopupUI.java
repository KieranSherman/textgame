package main.ui.components.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import main.ui.Developer;
import main.ui.Window;
import main.ui.components.display.background.PanelBackground;
import main.ui.components.input.AutoComplete;
import sound.SoundPlayer;
import util.Resources;

public class PopupUI {
	
	private PopupUI() {}
	
	private static String data;
	private static JFrame frame = Window.getFrame();
	
	public static void displayMessage(String message) {
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
				}
			}
		});
		
	    JButton button = new JButton("[ CLOSE ]");
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
	    	}
	    });
	    
	    JLabel label = new JLabel(message);
	    label.setForeground(Color.WHITE);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setVerticalAlignment(JLabel.CENTER);
	    label.setFont(Resources.DOS.deriveFont(15f));
	    
	    JPanel panel = new PanelBackground(Resources.commandBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		panel.add(button, BorderLayout.SOUTH);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	public static void promptInput(String prompt, boolean useAutoComplete) {
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
	    label.setForeground(Color.WHITE);
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
	    		data = textField.getText();
	    		dialog.dispose();
	    		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
	    	}
	    });
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
				} else
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					textField.setCaretPosition(textField.getText().length());
				}
			}

		});
	    inputPanel.add(textField, BorderLayout.CENTER);

	    if(useAutoComplete) {
	    	AutoComplete autoComplete = null;
	    	if(Developer.isDeveloperModeEnabled())
	    		autoComplete = new AutoComplete(textField, Resources.MASTER_COMMANDLIST);
	    	else
	    		autoComplete = new AutoComplete(textField, Resources.USER_COMMANDLIST);
	    	
			textField.getDocument().addDocumentListener(autoComplete);
			textField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "commit");
			textField.getActionMap().put("commit", autoComplete.new CommitAction());
	    }
		
	    JLabel in = new JLabel(" >> ");
	    in.setOpaque(false);
	    in.setFont(Resources.DOS.deriveFont(16f));
	    in.setForeground(Color.WHITE);
	    in.setHorizontalAlignment(JLabel.CENTER);
	    in.setVerticalAlignment(JLabel.CENTER);
	    inputPanel.add(in, BorderLayout.WEST);
	   
	    JPanel panel = new PanelBackground(Resources.commandBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(inputPanel, BorderLayout.CENTER);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	public static void promptChoice(String prompt, String[] choices) {
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
	    promptLabel.setForeground(Color.WHITE);
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
					data = choice;
				}
			});
		}
	    
	    JPanel buttonPanel = new JPanel(new GridLayout(1, choices.length, 0, 0));
	    buttonPanel.setBackground(Color.BLACK);
	    buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE));
	    for(int i = 0; i < buttons.length; i++)
	    	buttonPanel.add(buttons[i]);
	   
	    JPanel panel = new PanelBackground(Resources.commandBG);
		panel.setLayout(new BorderLayout());
		panel.add(promptLabel, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.CENTER);
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	public static String getData() {
		return data;
	}
	
}