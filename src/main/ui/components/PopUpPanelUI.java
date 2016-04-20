package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import util.Resources;

public class PopUpPanelUI {
	public  PopUpPanelUI(JFrame frame, String message) {
			JDialog dialog = new JDialog(frame, "Child", true);
			dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.WHITE));
			dialog.setLayout(new BorderLayout());
			dialog.setSize(300, 200);
			dialog.setLocationRelativeTo(frame);
		    JButton button = new JButton("Close");
		    button.setFont(Resources.USER_OUTPUT);
		    button.setBackground(Color.WHITE);
		    button.setForeground(Color.RED);
		    button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		    button.setFocusable(false);
		    button.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent e) {
		    	  dialog.dispose();
		      }
		    });
		    dialog.add(button, BorderLayout.SOUTH);
		    JLabel label = new JLabel(message);
		    label.setForeground(Color.WHITE);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setFont(Resources.USER_OUTPUT);
		    dialog.add(label, BorderLayout.CENTER);
		    dialog.setUndecorated(true);
		    dialog.getRootPane().setOpaque(false);
		    dialog.getContentPane().setBackground(new Color(0, 0, 0, 255));
		    dialog.setBackground(new Color(0, 0, 0, 255));
		    dialog.setVisible(true);
	}
}