package main.ui.components.display;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import main.ui.Window;

/**
 * Class extends {@link JSplitPane} to modify a split pane.
 * 
 * @author kieransherman
 *
 */
public class SplitPaneUI extends JSplitPane {
	private static final long serialVersionUID = 1L;
	
	private int dividerDragSize = 10;
    private int dividerDragOffset = 4;
    
    public SplitPaneUI() {
        setDividerSize(dividerDragSize);
        setContinuousLayout(true);
    }

	@Override
	public void doLayout() {
		super.doLayout();

        BasicSplitPaneDivider divider = ((BasicSplitPaneUI)getUI()).getDivider();
        Rectangle bounds = divider.getBounds();
        
        bounds.x -= dividerDragOffset;
        bounds.width = dividerDragSize;
        
        divider.setBounds(bounds);
    }
	
	@Override
	public void updateUI() {
        setUI(new SplitPaneWithZeroSizeDividerUI());
        revalidate();
    }

    private class SplitPaneWithZeroSizeDividerUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new ZeroSizeDivider(this);
        }
    }
	    
    private class ZeroSizeDivider extends BasicSplitPaneDivider {
		private static final long serialVersionUID = 1L;

		public ZeroSizeDivider(BasicSplitPaneUI ui) {
            super(ui);
            super.setBorder(null);
            setBackground(UIManager.getColor("controlShadow"));
            
            addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {
					Window.notes.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					Window.notes.setCursor(Cursor.getDefaultCursor());
				}
            });
        }

        @Override
        public void setBorder(Border border) {}

        @Override
        public void paint(Graphics g) {
            g.setColor(getBackground());
            g.drawLine(dividerDragOffset, 0, dividerDragOffset, getHeight() - 1);
        }
        
        @Override
        protected void dragDividerTo(int location) {
            super.dragDividerTo(location + dividerDragOffset);
        }

        @Override
        protected void finishDraggingTo(int location) {
        	super.finishDraggingTo(location + dividerDragOffset);
        }
    }
    
    
}
    

