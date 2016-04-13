package main.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;

import main.BootThread;
import main.ui.Window;
import util.Resources;

public class TextPaneDisplayUI extends Window {
	
	private static final long serialVersionUID = 1L;
	
	private TextPaneDisplayUI() {}

	public static JScrollPane createTextPane() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());

		doc = new DefaultStyledDocument();
		textPane = new JTextPane(doc);
		context = new StyleContext();
		style = context.addStyle("TextGame", null);
		
		textPane.setEditable(false);
		textPane.setFont(Resources.USER_OUTPUT);
		textPane.setBackground(new Color(15, 15, 15));
		textPane.setForeground(Color.WHITE);
		textPane.setMargin(new Insets(0, 10, 0, 10));
		textPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				textField.requestFocus();
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
		
		p.add(textPane, BorderLayout.CENTER);

		JPanel sidebar = new JPanel();
		sidebar.setLayout(new BorderLayout());
		sidebar.setPreferredSize(new Dimension(Resources.WIDTH/2-20, textPane.getHeight()));
		sidebar.setBackground(new Color(20, 20, 20));
		
		notes = new JTextPane();
		notes.setPreferredSize(sidebar.getPreferredSize());
		notes.setText("----these are your notes----");
		notes.setBackground(sidebar.getBackground());
		notes.setForeground(Color.WHITE);
		notes.setFont(Resources.USER_OUTPUT);
		notes.setCaretColor(Color.WHITE);
		notes.setSelectionColor(Color.GRAY);
		notes.setMargin(new Insets(10, 10, 0, 10));
		notes.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				notes.setBackground(new Color(45, 45, 45));
				sidebar.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				notes.setBackground(new Color(20, 20, 20));
				sidebar.setCursor(Cursor.getDefaultCursor());
			}
		});
		
		sidebar.add(notes, BorderLayout.CENTER);
		Border matteBorder = BorderFactory.createMatteBorder(0, 1, 0, 0, Color.WHITE);
		sidebar.setBorder(matteBorder);
		
		p.add(sidebar, BorderLayout.EAST);
		
		JScrollPane scroll = new JScrollPane(p);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUI(new ScrollBarUI());
		scroll.getHorizontalScrollBar().setUI(new ScrollBarUI());
		scroll.setBackground(new Color(15, 15, 15));
		scroll.setBorder(compoundBorder);
		
		BootThread.queueInfo("textPane loaded");
		
		return scroll;
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
