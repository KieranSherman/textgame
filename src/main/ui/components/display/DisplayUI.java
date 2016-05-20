package main.ui.components.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.ui.Window;
import main.ui.components.backgrounds.PanelBackground;
import main.ui.components.notifications.NotificationUI;
import main.ui.components.scrollbars.ScrollBarUI_Horizontal;
import main.ui.components.scrollbars.ScrollBarUI_Vertical;
import sound.SoundPlayer;
import util.Resources;

public class DisplayUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private static DisplayUI display;
	
	private static JTextArea terminalHead;
	private static JSplitPane splitPane;
	private static JPanel terminalPanel;
	
	private static int lines;
	
	private DisplayUI() {
		display = this;
	}
	
	public static void boot() {
		terminalHead.setText(
				"BUILD "+Resources.VERSION+"\n"
				+ System.getProperty("user.home").toUpperCase()+": *ACCESS GRANTED*\n\n"
				+ "\tKLETUS INDUSTRIES UNIFIED OPERATING SYSTEM\n"
				+ "\t  COPYRIGHT 3015-3067 KLETUS INDUSTRIES\n"
				+ "\t             -TERMINAL 1-");
		terminalHead.setForeground(new Color(102, 186, 49, 150));
		
		splitPane.setDividerLocation(splitPane.getWidth()-12);
		splitPane.setEnabled(true);
	}
	
	public static PanelBackground getTerminalPanel() {
		return (PanelBackground)terminalPanel;
	}

	public static Component createTextPane() {
		Window.doc = new DefaultStyledDocument();
		Window.terminal = new JTextPane(doc);
		Window.context = new StyleContext();
		Window.style = context.addStyle("TextGame", null);
		
		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(set, .2f);

		Window.terminal.setEditable(false);
		Window.terminal.setFont(Resources.DOS.deriveFont(13f));
		Window.terminal.setMargin(new Insets(30, 60, 0, 10));
		Window.terminal.setOpaque(false);
		Window.terminal.setSelectionColor(new Color(0,0,0,0));
		Window.terminal.setParagraphAttributes(set, false);
		Window.terminal.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				Window.input.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		Window.terminal.setHighlighter(null);
		
		Border linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		Border titledBorder = BorderFactory.createTitledBorder(linedBorder, "COMMLINK", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), Resources.DARK_RED);
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, terminal.getBorder());

		terminalPanel = new PanelBackground(Resources.terminalBG);
		terminalPanel.setOpaque(false);
		terminalPanel.setLayout(new BorderLayout());
		
		terminalHead = new JTextArea(
				System.getProperty("user.home").toUpperCase()+": *ACCESS DENIED*\n\n");
		terminalHead.setFont(Resources.DOS.deriveFont(13f));
		terminalHead.setForeground(new Color(255, 50, 50, 175));
		terminalHead.setMargin(new Insets(20, 60, 0, 20));
		terminalHead.setOpaque(false);
		terminalHead.setEditable(false);
		terminalHead.setHighlighter(null);
		
		terminalPanel.add(terminalHead, BorderLayout.NORTH);
		terminalPanel.add(Window.terminal, BorderLayout.CENTER);
		terminalPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
		
		JScrollPane scrollPane_COMMLINK = new JScrollPane(terminalPanel);
		scrollPane_COMMLINK.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_COMMLINK.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_COMMLINK.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_COMMLINK.setBorder(null);
		
		JPanel notesPanel = new JPanel();
		notesPanel.setOpaque(false);
		notesPanel.setLayout(new BorderLayout());
		
		Window.notes = new JTextPane();
		Window.notes.setOpaque(false);
		Window.notes.setLayout(new BorderLayout());
		Window.notes.setForeground(new Color(255, 255, 255, 175));
		Window.notes.setFont(Resources.DOS.deriveFont(16f));
		Window.notes.setCaretColor(Color.WHITE);
		Window.notes.setSelectionColor(Color.GRAY);
		Window.notes.setBorder(new EmptyBorder(0, 0, 0, 20));
		Window.notes.setText("[ ` ] { HELP }\n\n");
		notesPanel.add(Window.notes, BorderLayout.CENTER);
		
		JTextArea noteHead = new JTextArea(
				  "KLETUS INDUSTRIES UNIFIED NOTEPAD SYSTEM\n"
				+ "  COPYRIGHT 3015-3067 KLETUS INDUSTRIES\n"
				+ "             -NOTEPAD 1-");
		noteHead.setOpaque(false);
		noteHead.setFont(Resources.DOS.deriveFont(16f));
		noteHead.setForeground(new Color(255, 255, 255, 150));
		noteHead.setMargin(new Insets(20, 30, 40, 0));
		noteHead.setEditable(false);
		noteHead.setHighlighter(null);
		notesPanel.add(noteHead, BorderLayout.NORTH);
		
		JPanel rightComponent = new PanelBackground(Resources.notesBG);
		rightComponent.setLayout(new BorderLayout());
		
		JScrollPane scrollPane_NOTES = new JScrollPane(notesPanel);
		scrollPane_NOTES.setOpaque(false);
		scrollPane_NOTES.getViewport().setOpaque(false);
		scrollPane_NOTES.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane_NOTES.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_NOTES.getHorizontalScrollBar().setOpaque(false);
		scrollPane_NOTES.getVerticalScrollBar().setOpaque(false);
		scrollPane_NOTES.setBorder(null);
		
		rightComponent.add(scrollPane_NOTES, BorderLayout.CENTER);
		
		splitPane = new SplitPaneUI(display);
		splitPane.setLeftComponent(terminalPanel);
		splitPane.setRightComponent(rightComponent);
		splitPane.setBorder(compoundBorder);
		splitPane.setDividerLocation(Resources.WIDTH);
		splitPane.setEnabled(false);
		splitPane.setBackground(new Color(15, 15, 15));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
		mainPanel.setOpaque(false);
		mainPanel.add(NotificationUI.getNotificationPane(), BorderLayout.EAST);
		
		SoundPlayer.play("computerStartup");
		SoundPlayer.loop("computerHum");
		SoundPlayer.loop("computerHardDrive");
		SoundPlayer.loop("clock");
		
		return mainPanel;
	}
	
	public static void loadNotesHelp() {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(Resources.DIRECTORY+"src/files/reference/notes-help.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		String line;
		try {
			while((line = br.readLine()) != null)
				Window.notes.setText(Window.notes.getText()+line+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Window.notes.setCaretPosition(0);
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			
			if(str.contains("\n"))
				lines++;
			
			if(lines > 34) {
				Window.doc.remove(0, Window.doc.getText(0, Window.doc.getLength()).split("\n")[0].length()+1);
				lines--;
			}
			
			Window.terminal.setCaretPosition(Window.doc.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clear() {
		lines = 0;
		Window.terminal.setText("");
	}
	
}
