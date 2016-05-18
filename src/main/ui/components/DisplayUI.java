package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.ui.Window;
import main.ui.components.backgrounds.DisplayBackground;
import main.ui.components.backgrounds.NotesBackground;
import main.ui.components.notifications.NotificationPaneUI;
import main.ui.components.scrollbars.ScrollBarUI_Horizontal;
import sound.Sound;
import util.Resources;

public class DisplayUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private static DisplayUI display;
	
	public static JTextArea terminalHead;
	
	private DisplayUI() {
		display = this;
	}

	public static Component createTextPane() {
		Window.doc = new DefaultStyledDocument();
		Window.textPane = new JTextPane(doc);
		Window.context = new StyleContext();
		Window.style = context.addStyle("TextGame", null);
		
		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(set, .2f);

		Window.textPane.setEditable(false);
		Window.textPane.setFont(Resources.DOS);
		Window.textPane.setMargin(new Insets(30, 60, 0, 10));
		Window.textPane.setOpaque(false);
		Window.textPane.setSelectionColor(new Color(0,0,0,0));
		Window.textPane.setParagraphAttributes(set, false);
		Window.textPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				Window.textField.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		Window.textPane.setHighlighter(null);
		
		Border linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		Border titledBorder = BorderFactory.createTitledBorder(linedBorder, "COMMLINK", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), Resources.DARK_RED);
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, textPane.getBorder());

		Window.notes = new JTextPane();
		Window.notes.setLayout(new BorderLayout());
		Window.notes.setForeground(new Color(255, 255, 255, 175));
		Window.notes.setFont(Resources.DOS.deriveFont(12f));
		Window.notes.setCaretColor(Color.WHITE);
		Window.notes.setSelectionColor(Color.GRAY);
		Window.notes.setMargin(new Insets(10, 10, 0, 10));
		Window.notes.setOpaque(false);

		JPanel leftPanel = new DisplayBackground();
		leftPanel.setLayout(new BorderLayout());
		
		terminalHead = new JTextArea(
				System.getProperty("user.home").toUpperCase()+": *ACCESS GRANTED*\n\n"
				+ "\tKLETUS INDUSTRIES UNIFIED OPERATING SYSTEM\n"
				+ "\t  COPYRIGHT 3015-3067 KLETUS INDUSTRIES\n"
				+ "\t             -TERMINAL 1-");
		terminalHead.setFont(Resources.DOS);
		terminalHead.setForeground(new Color(102, 186, 49, 150));
		terminalHead.setMargin(new Insets(20, 60, 0, 0));
		terminalHead.setOpaque(false);
		terminalHead.setEditable(false);
		terminalHead.setHighlighter(null);
		
		leftPanel.add(terminalHead, BorderLayout.NORTH);
		leftPanel.add(Window.textPane, BorderLayout.CENTER);
		leftPanel.setBackground(new Color(15, 15, 15));
		
		JPanel rightPanel = new NotesBackground();
		rightPanel.setLayout(new BorderLayout());
		
		JTextArea noteHead = new JTextArea(
				  "KLETUS INDUSTRIES UNIFIED NOTEPAD SYSTEM\n"
				+ "  COPYRIGHT 3015-3067 KLETUS INDUSTRIES\n"
				+ "             -NOTEPAD 1-");
		noteHead.setFont(Resources.DOS);
		noteHead.setForeground(new Color(255, 255, 255, 150));
		noteHead.setMargin(new Insets(20, 30, 0, 0));
		noteHead.setOpaque(false);
		noteHead.setEditable(false);
		noteHead.setHighlighter(null);
		
		rightPanel.add(noteHead, BorderLayout.NORTH);
		rightPanel.add(Window.notes, BorderLayout.CENTER);
		rightPanel.setBackground(new Color(30, 30, 30));
		
		JScrollPane scrollPane_COMMLINK = new JScrollPane(leftPanel);
		scrollPane_COMMLINK.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_COMMLINK.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_COMMLINK.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_COMMLINK.setBorder(null);
		
		JScrollPane scrollPane_NOTES = new JScrollPane(rightPanel);
		scrollPane_NOTES.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_NOTES.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_NOTES.setBorder(null);
		
		JSplitPane splitPane = new SplitPaneUI(display);
		splitPane.setLeftComponent(scrollPane_COMMLINK);
		splitPane.setRightComponent(scrollPane_NOTES);
		splitPane.setBorder(compoundBorder);
		splitPane.setDividerLocation(Resources.WIDTH-Resources.WIDTH/3);
		splitPane.setBackground(new Color(15, 15, 15));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
		mainPanel.setOpaque(false);
		mainPanel.add(NotificationPaneUI.getNotificationPane(), BorderLayout.EAST);
		
		Sound.computerStartup.setGain(-20f);
		Sound.computerStartup.play();
		
		Sound.computerHum.setGain(-20f);
		Sound.computerHum.loop();
		
		Sound.computerHardDrive.setGain(-30f);
		Sound.computerHardDrive.loop();
		
		try {
			loadNotesHelp();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return mainPanel;
	}
	
	private static void loadNotesHelp() throws Exception {
		FileReader fr = new FileReader(new File("src/files/reference/notes-help.txt"));
		BufferedReader br = new BufferedReader(fr);
		
		String line;
		while((line = br.readLine()) != null)
			Window.notes.setText(Window.notes.getText()+line+"\n");
		
		br.close();
	}
	
	public static void setCursor(int cursorType) {
		Window.notes.setCursor(Cursor.getPredefinedCursor(cursorType));
	}
	
	/*
	 * Inserts text into the styled doc
	 */
	public static void insertTextToDoc(String str) {
		try {
			Window.doc.insertString(Window.doc.getLength(), str, style);
			Window.textPane.setCaretPosition(Window.doc.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
