package main.ui.components.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.misc.Developer;
import main.ui.Window;
import main.ui.components.display.background.PanelBackground;
import main.ui.components.display.notification.NotificationUI;
import main.ui.components.display.scrollbars.ScrollBarUI_Horizontal;
import main.ui.components.display.scrollbars.ScrollBarUI_Vertical;
import util.Resources;
import util.StringFormatter;

/**
 * This class consists exclusively of static methods that affect the display.
 * 
 * @author kieransherman
 * @see #initialize()
 * @see #createDisplay()
 * @see	#loadNotesHelp()
 * @see #insertTextToDoc(String)
 * @see #clear()
 *
 */
public class DisplayUI {
	
	public static JPanel notesPanel;
	private static JTextArea terminalHead;
	public static JSplitPane splitPane;
	
	private static int lines;
	
	// Prevent object instantiation
	private DisplayUI() {}
	
	/**
	 * Enables the split pane, and sets the terminal panel's header.
	 */
	public static void initialize() {
		terminalHead.setText(
				"BUILD "+Resources.CURRENT_VERSION+"\n"
				+ System.getProperty("user.home").toUpperCase()+": *ACCESS GRANTED*\n\n"
				+ "\tKLETUS INDUSTRIES UNIFIED OPERATING SYSTEM\n"
				+ "\t  COPYRIGHT 3015-3067 KLETUS INDUSTRIES\n"
				+ "\t             -TERMINAL 1-");
		terminalHead.setForeground(new Color(102, 186, 49, 150));
		
		splitPane.setDividerLocation(Window.terminal.getWidth()/2+15);
		splitPane.setEnabled(true);
		
		Developer.parseCommand("man man");
	}
	
	/**
	 * Creates and returns the {@link JPanel} display.
	 * 
	 * @return the display.
	 */
	public static JPanel createDisplay() {
		splitPane = new SplitPaneUI();
		splitPane.setLeftComponent(getTerminalPanel());
		splitPane.setRightComponent(getNotesPanel());
		splitPane.setBorder(Resources.getBorder("COMMLINK", new Color(255, 40, 40, 180)));
		splitPane.setDividerLocation(Integer.MAX_VALUE);
		splitPane.setBackground(new Color(15, 15, 15));
		splitPane.setEnabled(false);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.add(splitPane, BorderLayout.CENTER);
		NotificationUI.createNotificationDisplay(mainPanel, BorderLayout.EAST);
		
		return mainPanel;
	}
	
	/**
	 * Creates and returns the terminal panel.
	 */
	private static JPanel getTerminalPanel() {
		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(set, .2f);
		
		Window.doc = new DefaultStyledDocument();
		Window.context = new StyleContext();
		Window.style = Window.context.addStyle("TextGame", null);

		Window.terminal = new JTextPane(Window.doc);
		Window.terminal.setOpaque(false);
		Window.terminal.setEditable(false);
		Window.terminal.setFont(Resources.DOS.deriveFont(13f));
		Window.terminal.setMargin(new Insets(30, 60, 0, 10));
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
		
		terminalHead = new JTextArea(System.getProperty("user.home").toUpperCase()+": *ACCESS DENIED*\n\n");
		terminalHead.setFont(Resources.DOS.deriveFont(13f));
		terminalHead.setForeground(new Color(255, 50, 50, 175));
		terminalHead.setMargin(new Insets(20, 60, 0, 100));
		terminalHead.setOpaque(false);
		terminalHead.setEditable(false);
		terminalHead.setHighlighter(null);

		JPanel leftPanel = new PanelBackground(Resources.terminalBG);
		leftPanel.setOpaque(false);
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
		leftPanel.add(terminalHead, BorderLayout.NORTH);
		leftPanel.add(Window.terminal, BorderLayout.CENTER);
		
		return leftPanel;
	}
	
	/**
	 * Creates and returns the notes panel.
	 */
	private static JPanel getNotesPanel() {
		Window.notes = new JTextPane();
		Window.notes.setOpaque(false);
		Window.notes.setMargin(new Insets(0, 10, 0, 10));
		Window.notes.setLayout(new BorderLayout());
		Window.notes.setForeground(new Color(255, 255, 255, 175));
		Window.notes.setFont(Resources.DOS.deriveFont(16f));
		Window.notes.setCaretColor(Color.WHITE);
		Window.notes.setSelectionColor(Color.GRAY);
		Window.notes.setText("[ ` ] { HELP }\n\n");
		
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
		
		JPanel notesContainer = new JPanel();
		notesContainer.setOpaque(false);
		notesContainer.setLayout(new BorderLayout());
		notesContainer.add(Window.notes, BorderLayout.CENTER);
		notesContainer.add(noteHead, BorderLayout.NORTH);
		
		JScrollPane scrollPane_NOTES = new JScrollPane(notesContainer);
		scrollPane_NOTES.setOpaque(false);
		scrollPane_NOTES.getViewport().setOpaque(false);
		scrollPane_NOTES.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
		scrollPane_NOTES.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane_NOTES.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 8));
		scrollPane_NOTES.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_NOTES.getHorizontalScrollBar().setOpaque(false);
		scrollPane_NOTES.getVerticalScrollBar().setOpaque(false);
		scrollPane_NOTES.setBorder(null);
		
		notesPanel = new PanelBackground(Resources.notesBG);
		notesPanel.setLayout(new BorderLayout());
		notesPanel.add(scrollPane_NOTES, BorderLayout.CENTER);
		
		return notesPanel;
	}
	
	/**
	 * Load help into the notes panel.
	 */
	public static void loadNotesHelp() {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(Resources.HELP));
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
	
	/**
	 * Inserts a {@code String} into the terminal textpane.
	 * 
	 * @param str the {@code String} to insert.
	 */
	public synchronized static void insertTextToDoc(String str) {
		for(String s : StringFormatter.getWordWrap(str, 60)) {
			try {
				s += (s.contains("\n") ? "" : "\n");
				
				Window.doc.insertString(Window.doc.getLength(), s, Window.style);
				
				int lineCount = 0;
				for(int i = 0; i < s.length(); i++)
					if(s.charAt(i) == '\n')
						lineCount++;
				
				lines += lineCount;
				
				while(lines > 36) {
					Window.doc.remove(0, Window.doc.getText(0, Window.doc.getLength()).split("\n")[0].length()+1);
					lines--;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Clears the terminal textpane.
	 */
	public static void clear() {
		lines = 0;
		Window.terminal.setText("");
	}
	
}
