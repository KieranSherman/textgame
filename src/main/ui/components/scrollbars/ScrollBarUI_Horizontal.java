package main.ui.components.scrollbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/*
 * Class sets the UI for a scroll bar
 */
public class ScrollBarUI_Horizontal extends BasicScrollBarUI {
	
	protected JButton createZeroButton() {
	    JButton button = new JButton("zero button");
	    
	    Dimension zeroDim = new Dimension(0,0);
	    button.setPreferredSize(zeroDim);
	    button.setMinimumSize(zeroDim);
	    button.setMaximumSize(zeroDim);
	    
	    return button;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
	    return createZeroButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
	    return createZeroButton();
	}

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	g.setColor(new Color(255, 255, 255, 205));
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }
    
    
}