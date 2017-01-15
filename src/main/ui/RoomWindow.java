package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import util.Resources;

public class RoomWindow extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private JPanel canvas;

    public RoomWindow() {
        try {
            this.image = ImageIO.read(new File("src/files/images/room/door.png"));
        }catch(IOException ex) {
            Logger.getLogger(RoomWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.canvas = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //g.drawImage(image, 300, 75, null);
                g.setColor(new Color(60, 50, 50));
                g.fillRect(0, 500, 4000, 100);
                g.setColor(new Color(30, 30, 30));
                g.fillRect(995, 0, 10, 500);
                g.fillRect(1995, 0, 10, 500);
                g.fillRect(2995, 0, 10, 500);
                g.setColor(new Color(255, 40, 40, 180));
                g.setFont((Resources.DOS.deriveFont(20f)));
                FontMetrics fm = g.getFontMetrics();
                int w = fm.stringWidth("North");
                int h = fm.getAscent();
                g.drawString("North", 500 - w/2, h);
                w = fm.stringWidth("East");
                g.drawString("East", 1500 - w/2, h);
                w = fm.stringWidth("South");
                g.drawString("South", 2500 - w/2, h);
                w = fm.stringWidth("West");
                g.drawString("West", 3500 - w/2, h);
            }
        };
        canvas.setPreferredSize(new Dimension(4000, 500));
        JScrollPane sp = new JScrollPane(canvas);
        canvas.setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel p = new RoomWindow();
                JFrame f = new JFrame();
                f.add(p);
                f.setSize(1000, 600);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
            }
        });
    }
}