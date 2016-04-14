package main.ui.components;

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

public class JSplitPaneUI extends JSplitPane {
	private static final long serialVersionUID = 1L;
	
	private int dividerDragSize = 9;
    private int dividerDragOffset = 4;
    
    private TextPaneDisplayUI display;

    public JSplitPaneUI(TextPaneDisplayUI display) {
    	this.display = display;
    	
        setDividerSize(1);
        setContinuousLayout(true);
    }

	@Override
	public void doLayout() {
		super.doLayout();

        BasicSplitPaneDivider divider = ((BasicSplitPaneUI)getUI()).getDivider();
        Rectangle bounds = divider.getBounds();
        
        if(orientation == HORIZONTAL_SPLIT) {
            bounds.x -= dividerDragOffset;
            bounds.width = dividerDragSize;
        } else {
            bounds.y -= dividerDragOffset;
            bounds.height = dividerDragSize;
        }
        
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
					if(display != null)
						display.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if(display != null)
						display.setCursor(Cursor.getDefaultCursor());
				}
            });
        }

        @Override
        public void setBorder(Border border) {}

        @Override
        public void paint(Graphics g) {
            g.setColor(getBackground());
            if(orientation == HORIZONTAL_SPLIT)
                g.drawLine(dividerDragOffset, 0, dividerDragOffset, getHeight() - 1);
            else
                g.drawLine(0, dividerDragOffset, getWidth() - 1, dividerDragOffset);
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
    

