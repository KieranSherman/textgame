package util.out;

import java.awt.Color;

import javax.swing.text.StyleConstants;

import main.ui.Window;
import main.ui.components.display.DisplayUI;
import network.packet.Packet;
import network.packet.types.PacketTypes;
import sound.SoundPlayer;

/**
 * Class consists exclusively of static methods which append text to the {@link DisplayUI}.
 * 
 * @author kieransherman
 *
 */
public class DefaultLogger {
	
	// Prevent object instantiation
	private DefaultLogger() {}
	
	/**
	 * Append text to the end of the display.
	 * 
	 * @param line the {@code String} to append.
	 */
	public synchronized static void appendText(String line) {
		if(line == null)
			return;
		
		String newLine = "";
		
		for(String word : line.split("\\s+")) {
			Color color = Colorer.getColor(word);
			Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
			StyleConstants.setForeground(Window.style, alpha);
			newLine += (word+" ");
		}
		
		DisplayUI.insertTextToDoc(newLine);
	}
	
	/**
	 * Append colored text to the end of the display.
	 * 
	 * @param line the {@code String} to append.
	 * @param color the color of the line.
	 */
	public synchronized static void appendColoredText(String line, Color color) {
		if(line == null)
			return;
		
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(Window.style, alpha);
		DisplayUI.insertTextToDoc(line+"\n");
	}
	
	/**
	 * Gets a packet's text and appends it to the end of the display.
	 * 
	 * @param packet the packet to append.
	 */
	public synchronized static void appendPacket(Packet packet) {
		PacketTypes packetType = packet.getType();
		String line = (String)packet.getData();
		
		if(line == null)
			return;
		
		if(packetType == PacketTypes.ACTION) {
			appendText(line);
			return;
		}
		
		Color color = Colorer.getPacketColor(packetType);
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(Window.style, alpha);
		DisplayUI.insertTextToDoc(line+"\n");
		
		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
	}
	
}
