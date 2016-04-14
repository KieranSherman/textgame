package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.ui.Window;
import network.packet.types.Packet03Message;
import util.Resources;

public class TextFieldInputUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private TextFieldInputUI() {}
	
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
				TitledBorder.CENTER, TitledBorder.TOP, Resources.UI, Resources.DARK_GREEN);
		Border compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
		
		inputField.setBorder(compound);
		
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = textField.getText();

				appendText("> "+str);
				textField.setText("");
				
				if(!str.startsWith("!") && str != null)
					adapter.sendPacket(new Packet03Message("] "+str));
			}
		});
		
		return inputField;
	}
	
}
