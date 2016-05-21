package main.ui.components;

import java.awt.Color;

import main.Main;
import main.ui.Window;
import main.ui.components.display.DisplayUI;
import main.ui.components.misc.PopupUI;
import main.ui.components.notifications.NotificationUI;
import network.Adapter;
import network.packet.types.Packet03Message;
import sound.SoundPlayer;
import util.Action;
import util.Resources;

public class Developer {
	
	private static boolean dev = false;
	
	private Developer() {}
	
	public static void parseCommand(String str) {
		if(str == null)
			return;
		
		str = str.toLowerCase();
		String [] args = str.split("\\s+");
		
		if(args[0].equals("server")) {
			String port = "9999";
			
			for(String s : args)
				if(s.contains("p:"))
					port = s.substring(s.indexOf(":")+1);
			
			Adapter.createServer(Integer.parseInt(port));
			Window.getFrame().setTitle(Resources.VERSION+" | running server");
		}
		else
		if(args[0].equals("client")) {
			String address = "localhost";
			String port = "9999";
			
			for(String s : args)
				if(s.contains("a:"))
					address = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("p:"))
					port = s.substring(s.indexOf(":")+1);
						
			Adapter.createClient(address, Integer.parseInt(port));
			Window.getFrame().setTitle(Resources.VERSION+" | running client");
		}
		else
		if(args[0].equals("block") && dev) {
			String display = "false";
			
			for(String s : args)
				if(s.contains("d:"))
					display = s.substring(s.indexOf(":")+1);
			
			Adapter.block(Boolean.parseBoolean(display));
		}
		else
		if(args[0].equals("clear")) {
			DisplayUI.clear();
		}
		else
		if(args[0].equals("popup") && dev) {
			PopupUI.promptInput("POPUP TEST");
		}
		else
		if(args[0].equals("notify") && dev) {
			String message = "NOTIFICATION TEST";
			int time = 2000;
			int repeat = 1;
			boolean random = false;
			
			for(String s : args)
				if(s.contains("m:"))
					message = s.substring(s.indexOf(":")+1);
			
			for(String s : args)
				if(s.contains("t:"))
					time = Integer.parseInt(s.substring(s.indexOf(":")+1));
			
			for(String s : args)
				if(s.contains("x"))
					repeat = Integer.parseInt(s.substring(s.indexOf("x")+1));
			
			for(String s : args)
				if(s.contains("r:"))
					random = Boolean.parseBoolean(s.substring(s.indexOf(":")+1));
			
			for(int i = 0; i < repeat; i++)
				if(random)
					NotificationUI.queueNotification(message.toUpperCase()+" "+(i+1), (int)(time+Math.random()*10000), null, true);
				else
					NotificationUI.queueNotification(message.toUpperCase()+" "+(i+1), time, null, true);
		}
		else
		if(args[0].equals("status")) {
			Adapter.status();
		}
		else
		if(args[0].equals("logout")) {
			Adapter.close();
			Window.getFrame().setTitle(Resources.VERSION);
		}
		else
		if(args[0].equals("notes")) {
			Adapter.sendPacket(new Packet03Message("START >>>>>>>\n"+Window.notes.getText()+"\n<<<<<<< END"));
			Window.appendColoredText("[sent notes]", Color.GRAY);
		}
		else
		if(args[0].equals("help")) {
			DisplayUI.loadNotesHelp();
		}
		else
		if(args[0].equals("dev")) {
			if(args.length == 2 && args[1].equals("kletus")) {
				Window.appendColoredText("[developer commands enabled]", Color.GREEN);
				dev = true;
			} else {
				PopupUI.promptInput("ENTER PASSWORD");
				if(!PopupUI.getData().equalsIgnoreCase("kletus")) {
					Window.appendColoredText("[incorrect password]", Color.RED);
					SoundPlayer.play("error");
				}
				else {
					Window.appendColoredText("[developer commands enabled]", Resources.DARK_GREEN);
					dev = true;
				}
			}
		}
		else
		if(args[0].equals("remix") && dev) {
			SoundPlayer.play("remix");
			
			Action action = new Action() {
				@Override
				public void execute() {
					NotificationUI.queueNotification("RAVE RIGHT", 11000, null, false);
					Window.appendColoredText("/o/", Color.CYAN);
					Window.appendColoredText("\\o/", Color.CYAN);
				}
			};
			
			NotificationUI.queueNotification("RAVE LEFT", 8300, action, false);
			Window.appendColoredText("\\o\\", Color.CYAN);
			Window.appendColoredText("\\o/", Color.CYAN);
		}
		else
		if(args[0].equals("restart") && dev) {
			Window.getFrame().dispose();
			Main.restart();
		}
		else {
			Window.appendColoredText("[unrecognized command: "+str+"]", Color.RED);
			SoundPlayer.play("error");
		}
	}

}
