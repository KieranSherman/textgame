package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
		Window.textPane.setAutoscrolls(true);
		
		Border linedBorder = BorderFactory.createLineBorder(Color.WHITE);
		Border titledBorder = BorderFactory.createTitledBorder(linedBorder, "COMMLINK", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.UI, Resources.DARK_RED);
		Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, textPane.getBorder());

		Window.notes = new JTextPane();
		Window.notes.setText("----dev command list----\n\n"
						   + "!server\n\t[start a server]\n\n"
						   + "!client (address) (port)\n\t[start client]\n\n"
						   + "!block (boolean: showBlocked)\n\t[un/block incoming connections]\n\n"
						   + "!clear\n\t[clear the display]\n\n"
						   + "!status\n\t[report network status]\n\n"
						   + "![text]\n\t[send an action]\n\n"
						   + "!notes\n\t[send your notes]\n\n"
						   + "!logout\n\t[close network connections]\n");
		Window.notes.setBackground(new Color(20, 20, 20));
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
				notes.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Window.notes.setBackground(new Color(20, 20, 20));
				notes.setCursor(Cursor.getDefaultCursor());
			}
		});
		Window.notes.setAutoscrolls(true);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(Window.textPane, BorderLayout.CENTER);
		leftPanel.setBackground(new Color(15, 15, 15));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(Window.notes, BorderLayout.CENTER);
		rightPanel.setBackground(new Color(15, 15, 15));
		
		JScrollPane scrollPane_COMMLINK = new JScrollPane(leftPanel);
		scrollPane_COMMLINK.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_COMMLINK.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_COMMLINK.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane_COMMLINK.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_COMMLINK.setBackground(new Color(15, 15, 15));
		
		JScrollPane scrollPane_NOTES = new JScrollPane(rightPanel);
		scrollPane_NOTES.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_NOTES.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane_NOTES.getHorizontalScrollBar().setUI(new ScrollBarUI_Horizontal());
		scrollPane_NOTES.setBackground(new Color(15, 15, 15));
		
		JSplitPane splitPane = new JSplitPaneUI(display);
		splitPane.setLeftComponent(scrollPane_COMMLINK);
		splitPane.setRightComponent(scrollPane_NOTES);
		splitPane.setBorder(compoundBorder);
		splitPane.setBackground(new Color(15, 15, 15));
		splitPane.setDividerLocation(Resources.WIDTH/2);

		return splitPane;
	}
	
	public static void setCursor(int cursorType) {
		notes.setCursor(Cursor.getPredefinedCursor(cursorType));
	}
	
	/*
	 * Inserts text into the styled doc
	 */
	public static void insertTextToDoc(String str) {
		try {
			if(doc != null) {
				doc.insertString(doc.getLength(), str, style);
				textPane.setCaretPosition(doc.getLength());
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
