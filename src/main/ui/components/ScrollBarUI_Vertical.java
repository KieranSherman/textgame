package main.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import util.Resources;

/*
 * Class sets the UI for a scroll bar
 */
public class ScrollBarUI_Vertical extends BasicScrollBarUI {
	
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
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(new Color(15, 15, 15));
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	g.setColor(Resources.DARK_RED);
        g.fillRect(thumbBounds.x, thumbBounds.y, 3, thumbBounds.height);
    }
    
}