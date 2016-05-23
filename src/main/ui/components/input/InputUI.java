package main.ui.components.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.ui.Window;
import network.Adapter;
import network.packet.types.Packet03Message;
import sound.SoundPlayer;
import util.Resources;
import util.out.DefaultLogger;

public class InputUI {
	
	private static List<String> inputHistory = new ArrayList<String>();
	private static int historyIndex;
	
	// Prevent object instantiation
	private InputUI() {}
	
	/**
	 * Creates and returns the {@link JPanel} input panel containing a {@link JTextField}.
	 * 
	 * @return the input's JPanel
	 */
	public static JPanel createInput() {
		Window.input = new JTextField();
		Window.input.setFocusTraversalKeysEnabled(false);
		Window.input.setFont(Resources.USER_INPUT);
		Window.input.setBackground(new Color(15, 15, 15));
		Window.input.setForeground(Color.WHITE);
		Window.input.setCaretColor(Color.WHITE);
		Window.input.setBorder(null);
		Window.input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = Window.input.getText();
				
				if(!str.equals("")) {
					inputHistory.add(str);
					historyIndex = inputHistory.size();
				}

				DefaultLogger.appendText("> "+str);
				Window.input.setText("");
				
				SoundPlayer.play("key"+((int)(Math.random()*10)+1));
				
				if(str != null && !str.equals(""))
					Adapter.sendPacket(new Packet03Message(str));
			}
		});
		Window.input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP) {
					if (historyIndex > 0) {
						historyIndex--;
						Window.input.setText(inputHistory.get(historyIndex));
					}
				}
				else
				if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					Window.input.setText("");
					if (historyIndex < inputHistory.size()-1) {
						historyIndex++;
						Window.input.setText(inputHistory.get(historyIndex));
					} else {
						Window.input.setText("");
						historyIndex = inputHistory.size();
					}
				}
			}
		});
		Window.input.setEnabled(false);
		
		JLabel promptText = new JLabel("out:: ");
		promptText.setForeground(Color.WHITE);
		promptText.setFont(Resources.USER_INPUT);
		
		Border lineB = BorderFactory.createLineBorder(Color.WHITE);
		Border b = BorderFactory.createTitledBorder(lineB, "COMMS", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), Resources.DARK_GREEN);
		Border compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(new Color(15, 15, 15));
		inputPanel.setForeground(Color.WHITE);
		inputPanel.add(promptText, BorderLayout.WEST);
		inputPanel.add(Window.input, BorderLayout.CENTER);
		inputPanel.setBorder(compound);
		
		return inputPanel;
	}
	
}
