package util.out;

import java.awt.Color;

import main.ui.Window;
import network.packet.Packet;

/*
 * Logger class has direct access to the Game's Window
 */
public class Logger {
	
	public void appendPacket(Packet packet) {
		Window.appendPacket(packet);
	}
	
	public void appendText(String str) {
		Window.appendText(str);
	}
	
	public void appendText(String str, Color color) {
		Window.appendColoredText(str, color);
	}
	
}
