package util.out;

import main.ui.Window;
import network.packet.PacketTypes;

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
	public void appendPacketText(PacketTypes packetType, String str) {
		str = str.substring(Formatter.getFormat(packetType).length());

		if(packetType == PacketTypes.ACTION) {
			appendText("[THEM] "+str);
			return;
		}
		
		window.appendPacketText(packetType, str);
	}
	
	public void appendText(String str) {
		window.appendText(str);
	}
	
}
