package main.ui.components.status;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.ui.components.scrollbars.ScrollBarUI_Vertical;
import main.ui.components.status.renderer.MyCellRenderer;
import util.Action;
import util.Resources;

public class StatusUI {
	
	private static DefaultListModel<Action> listModel;
	
	private StatusUI() {}
	
	public static JPanel createStatusPane() {
		listModel = new DefaultListModel<Action>();
		
		JList<Action> list = new JList<Action>(listModel);
		list.setOpaque(false);
		list.setCellRenderer(new MyCellRenderer());
		list.setFont(Resources.DOS.deriveFont(16f));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
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
		list.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent evt) {}
			
			@Override
			public void mouseDragged(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<Object> list = (JList<Object>)evt.getSource();
		    	int index = list.locationToIndex(evt.getPoint());
		    	
		    	if(!list.getCellBounds(index, index).contains(evt.getPoint()))
		    		list.clearSelection();
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
	
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.add(scrollPane, BorderLayout.SOUTH);
		
		return mainPanel;
	}
	
	public static void addStatus(Action element) {
		listModel.addElement(element);
	}
	
	public static Action getStatus(int index) {
		if(index == -1)
			return null;
		
		return listModel.get(index);
	}
	
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

}
