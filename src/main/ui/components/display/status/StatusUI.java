package main.ui.components.display.status;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.ui.components.display.notification.NotificationUI;
import main.ui.components.display.scrollbars.ScrollBarUI_Vertical;
import main.ui.components.display.status.renderer.CellRendererUI;
import util.Action;
import util.Resources;

/**
 * This class consists exclusively of static methods that affect the status display.
 * 
 * @author kieransherman
 * @see #createStatusDisplay(JPanel, String)
 * @see #removeStatusDisplay()
 * @see #addStatus(Action)
 * @see #getStatus(int)
 * @see #removeStatus(String)
 *
 */

public class StatusUI {
	
	private static DefaultListModel<Action> listModel;
	private static JPanel mainPanel;
	
	static {
		listModel = new DefaultListModel<Action>();
	}
	
	// Prevent object instantiation
	private StatusUI() {}
	
	/**
	 * Adds a status panel to the specified panel at a {@link BorderLayout} position.
	 * 
	 * @param addToPanel the panel to add the notification panel to.
	 * @param borderLayout the {@link BorderLayout} position.
	 */
	public static void createStatusDisplay(JPanel addToPanel, String borderLayout) {
		JList<Action> list = new JList<Action>(listModel);
		list.setOpaque(false);
		list.setCellRenderer(new CellRendererUI());
		list.setFont(Resources.DOS.deriveFont(16f));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent evt) {
		    	@SuppressWarnings("unchecked")
				JList<Object> list = (JList<Object>)evt.getSource();
		    	int index = list.locationToIndex(evt.getPoint());
		    	
		    	if(!list.getCellBounds(index, index).contains(evt.getPoint())) {
		    		list.clearSelection();
		    		return;
		    	}
		    	
		        if (evt.getClickCount() == 3)
		            getStatus(index).execute();
		    }
		});
		list.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int index;
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					index = list.getSelectedIndex();
					
					Action action = getStatus(index);
					if(action != null)
						action.execute();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setPreferredSize(new Dimension(200, 275));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
		scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI_Vertical());
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setBorder(Resources.getBorder("SERVER", new Color(40, 190, 230, 180)));
	
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.add(scrollPane, BorderLayout.SOUTH);
		
		addToPanel.add(mainPanel, borderLayout);
		
		NotificationUI.setNotificationCapacity(6);
	}
	
	/**
	 * Removes the status display from the given panel.
	 * 
	 * @param removeFromPanel the {@link JPanel} containing the status display.
	 */
	public static void removeStatusDisplay(JPanel removeFromPanel) {
		removeFromPanel.remove(mainPanel);
		NotificationUI.setNotificationCapacity(10);
	}
	
	/**
	 * Adds an {@link Action} object to the list.
	 * 
	 * @param element the element to add to the list.
	 */
	public static void addStatus(Action element) {
		listModel.addElement(element);
	}
	
	/**
	 * Returns the {@link Action} object at an index.
	 * 
	 * @param index the index of the action.
	 * @return the action object at index.
	 */
	public static Action getStatus(int index) {
		if(index == -1)
			return null;
		
		return listModel.get(index);
	}
	
	/**
	 * Removes the given {@link Action} object from the list, determined by the {@code toString()}
	 * return of the object.
	 * 
	 * @param element the element to remove.
	 */
	public static void removeStatus(String element) {
		boolean found = false;
 		int i;
		for(i = 0; i < listModel.size(); i++) {
			Action a = listModel.get(i);
			if(a.toString().equals(element)) {
				found = true;
				break;
			}
		}
		
		if(found)
			listModel.remove(i);
	}
	
	/**
	 * Returns the {@link JPanel} of the {@link StatusUI}.
	 * 
	 * @return the status JPanel.
	 */
	public static JPanel getPanel() {
		return mainPanel;
	}

}
