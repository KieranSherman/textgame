package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;

import main.ui.Window;
import util.Resources;

public class TextPaneDisplayUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private static JPanel notesPanel;
	private static TextPaneDisplayUI display;
	
	private TextPaneDisplayUI() {
		display = this;
	}

	public static Component createTextPane() {
		Window.doc = new DefaultStyledDocument();
		Window.textPane = new JTextPane(doc);
		Window.context = new StyleContext();
		Window.style = context.addStyle("TextGame", null);
		
		Window.textPane.setEditable(false);
		Window.textPane.setFont(Resources.USER_OUTPUT);
		Window.textPane.setBackground(new Color(15, 15, 15));
		Window.textPane.setForeground(Color.WHITE);
		Window.textPane.setMargin(new Insets(0, 10, 0, 10));
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
		
		Border linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		Border titledBorder = BorderFactory.createTitledBorder(linedBorder, "COMMLINK", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.UI, Resources.DARK_RED);
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, textPane.getBorder());
		Border matteBorder = BorderFactory.createMatteBorder(1, 1, 0, 0, Color.WHITE);

		notesPanel = new JPanel();
		notesPanel.setLayout(new BorderLayout());
		notesPanel.setPreferredSize(new Dimension(300, textPane.getHeight()));
		notesPanel.setBackground(new Color(20, 20, 20));
		notesPanel.setBorder(matteBorder);
		
		Window.notes = new JTextPane();
		Window.notes.setPreferredSize(notesPanel.getPreferredSize());
		Window.notes.setText("----dev command list----\n\n"
						   + "!server\n\t[start a server]\n\n"
						   + "!client (address) (port)\n\t[start client]\n\n"
						   + "!block (boolean: showBlocked)\n\t[un/block incoming connections]\n\n"
						   + "!clear\n\t[clear the display]\n\n"
						   + "!status\n\t[report network status]\n\n"
						   + "![text]\n\t[send an action]\n\n"
						   + "!notes\n\t[send your notes\n\n"
						   + "!logout\n\t[close network connections]\n");
		Window.notes.setBackground(notesPanel.getBackground());
		Window.notes.setForeground(Color.WHITE);
		Window.notes.setFont(Resources.USER_OUTPUT);
		Window.notes.setCaretColor(Color.WHITE);
		Window.notes.setSelectionColor(Color.GRAY);
		Window.notes.setMargin(new Insets(10, 10, 0, 10));
		Window.notes.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				Window.notes.setBackground(new Color(30, 30, 30));
				notesPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Window.notes.setBackground(new Color(20, 20, 20));
				notesPanel.setCursor(Cursor.getDefaultCursor());
			}
		});
		notesPanel.add(Window.notes);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(Window.textPane, BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(notesPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(leftPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
		scrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
		scrollPane.setBackground(new Color(15, 15, 15));
		
		JSplitPane splitPane = new JSplitPaneUI(display);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(rightPanel);
		splitPane.setBorder(compoundBorder);
		splitPane.setBackground(new Color(15, 15, 15));
		splitPane.setDividerLocation(Resources.WIDTH/2);
		
		return splitPane;
	}
	
	public static void setCursor(int cursorType) {
		notesPanel.setCursor(Cursor.getPredefinedCursor(cursorType));
	}
	
	/*
	 * Inserts text into the styled doc
	 */
	public static void insertTextToDoc(String str) {
		try {
			if(doc != null)
				doc.insertString(doc.getLength(), str, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
