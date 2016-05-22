package util.out;

import java.awt.Color;

import javax.swing.text.StyleConstants;

import main.ui.Window;
import main.ui.components.display.DisplayUI;
import network.packet.Packet;
import network.packet.types.PacketTypes;
import sound.SoundPlayer;

/*
 * Logger class has direct access to the Game's Window
 */
public class Logger {
	
	private Logger() {}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc()
	 */
	public synchronized static void appendText(String str) {
		if(str == null)
			return;
		
		for(String word : str.split("\\s+")) {
			Color color = Colorer.getColor(word);
			Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
			StyleConstants.setForeground(Window.style, alpha);
			DisplayUI.insertTextToDoc(word+" ");
		}
		
		if(!str.contains("\n"))
			DisplayUI.insertTextToDoc("\n");
	}
	
	/*
	 * Appends str to the end of textPane with set color;
	 * acts as filter to method: insertTextToDoc();
	 */
	public synchronized static void appendColoredText(String str, Color color) {
		if(str == null)
			return;
		
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(Window.style, alpha);
		DisplayUI.insertTextToDoc(str+"\n");
	}
	
	/*
	 * Appends str to the end of textPane; acts as
	 * filter to method: insertTextToDoc();
	 * exclusively for the PacketParser
	 */
	public synchronized static void appendPacket(Packet packet) {
		PacketTypes packetType = packet.getType();
		String str = (String)packet.getData();
		
		if(str == null)
			return;
		
		if(packetType == PacketTypes.ACTION) {
			appendText(str);
			return;
		}
		
		Color color = Colorer.getPacketColor(packetType);
		Color alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 160);
		StyleConstants.setForeground(Window.style, alpha);
		DisplayUI.insertTextToDoc(str+"\n");
		
		SoundPlayer.play("key"+((int)(Math.random()*10)+1));
	}
	
}
