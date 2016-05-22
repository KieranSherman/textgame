package main.ui.components.display.status.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import util.Resources;

/**
 * Class extends {@link JLabel} and implements {@link ListCellRenderer} to modify how a {@link JList}
 * renders its elements.
 * 
 * @author kieransherman
 *
 */
public class CellRendererUI extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = 1L;

	public CellRendererUI() {
        super.setOpaque(true);
    }

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    	super.setText(value.toString());
    	super.setFont(Resources.DOS.deriveFont(14f));
    	
    	CompoundBorder compound = BorderFactory.createCompoundBorder(
    			BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE), new EmptyBorder(4, 7, 4, 7));
    	
    	super.setBorder(compound);

        Color background, foreground;

        if(isSelected) {
            background = new Color(200, 30, 50, 150);
            foreground = Color.WHITE;
        } else {
            background = new Color(40, 190, 230, 180);
            foreground = Color.BLACK;
        }

        super.setBackground(background);
        super.setForeground(foreground);
        
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setOpaque(false);
    	panel.setBorder(new EmptyBorder(4, 0, 4, 0));
        panel.add(this, BorderLayout.CENTER);

        return panel;
    }
    
}

