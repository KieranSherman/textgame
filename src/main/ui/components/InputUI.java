package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.ui.Window;
import network.packet.types.Packet03Message;
import sound.Sound;
import util.Resources;

public class InputUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private InputUI() {}
	
	public static Component createTextField() {
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
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), Resources.DARK_GREEN);
		Border compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
		
		inputField.setBorder(compound);
		
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = textField.getText();

				appendText("> "+str);
				textField.setText("");
				
				if(!str.startsWith("!") && str != null)
					adapter.sendPacket(new Packet03Message(str));
			}
		});
		
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					int ran = (int)(Math.random()*10);
					Sound.keyboardKeys[ran].play();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		return inputField;
	}
	
}
