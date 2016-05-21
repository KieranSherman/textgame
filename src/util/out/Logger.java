package util.out;

import java.awt.Color;

import main.ui.Window;
import network.packet.Packet;

/*
 * Logger class has direct access to the Game's Window
 */
public class Logger {
	
	private Logger() {}
	
	public static void appendPacket(Packet packet) {
		Window.appendPacket(packet);
	}
	
	public static void appendText(String str) {
		Window.appendText(str);
	}
	
	public static void appendText(String str, Color color) {
		Window.appendColoredText(str, color);
	}
	
}
