package util.out;

import java.awt.Color;

import main.ui.Window;
import network.packet.Packet;
import network.packet.types.PacketTypes;

/*
 * Logger class has direct access to the Game's Window
 */
public class Logger {
	
	private static Window window;
	
	public Logger(Window window) {
		Logger.window = window;
	}
	
	/*
	 * appends text to the text pane
	 */
	public void appendPacket(Packet packet) {
		PacketTypes packetType = packet.getType();
		String str = (String)packet.getData();

		if(packetType == PacketTypes.ACTION) {
			appendText("[THEM] "+str);
			return;
		}
		
		window.appendPacket(packet);
	}
	
	public void appendText(String str) {
		Window.appendText(str);
	}
	
	public void appendText(String str, Color color) {
		Window.appendColoredText(str, color);
	}
	
}
