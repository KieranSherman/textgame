package main.ui.components.misc;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.EmptyBorder;

import main.ui.Window;
import main.ui.components.backgrounds.PanelBackground;
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
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}
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
	
	public static void getInput(String prompt) {
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
	    label.setBorder(new EmptyBorder(12, 0, 12, 0));
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
	    textField.setCaretColor(Color.WHITE);
	    textField.setFont(Resources.DOS.deriveFont(16f));
	    textField.setForeground(Color.WHITE);
	    textField.setBorder(null);
	    textField.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		data = textField.getText();
	    		dialog.dispose();
	    		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
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
	   
	    JPanel panel = new PanelBackground(Resources.commandBG);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(inputPanel, BorderLayout.CENTER);
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialog.dispose();
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});
	    
	    dialog.add(panel, BorderLayout.CENTER);
	    dialog.getRootPane().setOpaque(false);
	    dialog.setVisible(true);
	}
	
	public static String getData() {
		return data;
	}
	
}