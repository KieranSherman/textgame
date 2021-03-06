package main.ui.components.display.manual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.ui.components.display.DisplayUI;
import util.Resources;

/**
 * Class consists exclusively of static methods to affect the manual display.
 * 
 * @author kieransherman
 *
 */
public class Manual {
	
	private static boolean open = false;
	private static JTextArea manualDisplay;
	private static JPanel manualPanel;
	
	// Prevent object instantiation
	private Manual() {}
	
	static {
		manualDisplay = new JTextArea();
		manualDisplay.setBackground(new Color(0, 0, 0, 120));
		manualDisplay.setMargin(new Insets(20, 20, 10, 50));
		manualDisplay.setLineWrap(true);
		manualDisplay.setWrapStyleWord(true);
		manualDisplay.setFont(Resources.MAN_DISPLAY);
		manualDisplay.setEditable(false);
		manualDisplay.setFocusable(false);
		manualDisplay.setForeground(Color.WHITE);
		manualDisplay.setHighlighter(null);
		
		JButton button = new JButton("CLOSE");
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.setBorder(null);
		button.setFocusable(false);
		button.setFont(Resources.DOS);
		button.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manual.removeManualPanel();
				Manual.setText("");
			}
		});
		
		manualPanel = new JPanel(new BorderLayout());
		manualPanel.setOpaque(false);
		manualPanel.setBorder(Resources.getBorder("MANUAL", Color.WHITE));
		manualPanel.add(manualDisplay, BorderLayout.CENTER);
		manualPanel.add(button, BorderLayout.SOUTH);
		manualPanel.setFocusable(false);
		manualPanel.setMinimumSize(new Dimension(300, Integer.MAX_VALUE));
	}
	
	/**
	 * Show the manual panel.
	 */
	public static void showManualPanel() {
		if(open)
			return;
		
		DisplayUI.notesPanel.add(manualPanel, BorderLayout.SOUTH);
		DisplayUI.notesPanel.revalidate();
		open = true;
	}
	
	/**
	 * Remove the manual panel.
	 */
	public static void removeManualPanel() {
		DisplayUI.notesPanel.remove(manualPanel);
		DisplayUI.notesPanel.revalidate();
		open = false;
	}
	
	/**
	 * Set the manual's text.
	 * 
	 * @param str the text to set.
	 */
	public static void setText(String str) {
		manualDisplay.setText(str);
	}
	
	/**
	 * Returns the manual for the given command.
	 * 
	 * @param str the command.
	 * @return the manual entry for the command.
	 */
	public static String getManualEntry(String str) {
		ArrayList<String> manList = Resources.MAN_LIST_ENTRIES;
		
		int indexOfCommand = -1;
		
		for(int i = 0; i < manList.size(); i++)
			if(manList.get(i).trim().startsWith(str)) {
				indexOfCommand = i;
				break;
			}
		
		if(indexOfCommand == -1)
			return "unrecognized command: "+str+"\n";
		
		String header = manList.get(indexOfCommand).split("\n")[1];
		String tagHeader = "";
		String regex = "";
		ArrayList<String> tags = new ArrayList<String>();

		for(String tagID : header.split("\\s+")) {
			String tag = Resources.getTagString(tagID);
			
			if(tag != null) {
				tags.add(tag);
				regex += tagID+" ";
			}
		}
		
		tagHeader = (tags.isEmpty() ? "" : " "+tags.toString().replaceAll("[\\[\\]]", "").replace(",", "  "))+"\n";
		
		String entry = "";
		
		if(!regex.equals("")) {
			for(String s : regex.split(" "))
				entry = manList.get(indexOfCommand).replace(s, "");
		
			return tagHeader.trim()+"\n"+entry;
		}
		
		return manList.get(indexOfCommand);
	}

}
