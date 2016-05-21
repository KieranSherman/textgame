package main.ui;

import java.awt.Color;

import main.ui.components.display.DisplayUI;
import main.ui.components.display.notification.NotificationUI;
import main.ui.components.popup.PopupUI;
import network.Adapter;
import network.packet.types.Packet03Message;
import network.server.ServerModifier;
import sound.SoundPlayer;
import util.Action;
import util.Resources;
import util.out.Logger;

public class Developer {
	
	private static boolean developerMode = false;
	
	private Developer() {}
	
	public static void parseCommand(String str) {
		if(str == null)
			return;

		str = str.toLowerCase();
		String [] args = str.split("\\s+");
		
		if(!Developer.userCommand(args) && !Developer.developerCommand(args)) {
			Logger.appendColoredText("[unrecognized command]", Color.RED);
			SoundPlayer.play("error");
		}

	}
	
	public static boolean userCommand(String[] args) {
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
		if(args[0].equals("clear")) {
			DisplayUI.clear();
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
			Logger.appendColoredText("[sent notes]", Color.GRAY);
		}
		else
		if(args[0].equals("help")) {
			DisplayUI.loadNotesHelp();
		}
		else
		if(args[0].equals("devmode_set")) {
			String devMode = "false";
			
			if(args.length == 2)
				devMode = args[1];
			
			developerMode = Boolean.parseBoolean(devMode);
			
			if(developerMode)
				Logger.appendColoredText("[developer commands enabled]", Resources.DARK_GREEN);
			else
				Logger.appendColoredText("[developer commands disabled]", Resources.DARK_GREEN);
			
			return true;
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public static boolean developerCommand(String[] args) {
		if(!developerMode)
			return false;
		
		if(args[0].equals("local_host_maximum")) {
			int localHostMaximum = 1;
			
			if(args.length == 2)
				localHostMaximum = Integer.parseInt(args[1]);
		
			ServerModifier.setLocalHostMaximum(localHostMaximum);
			Logger.appendColoredText("[local_host_maximum set to "+localHostMaximum+"]", Resources.DARK_GREEN);
		}
		else
		if(args[0].equals("same_client_maximum")) {
			int sameClientMaximum = 1;
			
			if(args.length == 2)
				sameClientMaximum = Integer.parseInt(args[1]);
		
			ServerModifier.setSameClientMaximum(sameClientMaximum);
			Logger.appendColoredText("[same_client_maximum set to "+sameClientMaximum+"]", Resources.DARK_GREEN);
		}
		else
		if(args[0].equals("client_connection_maximum")) {
			int clientConnectionMaximum = 1;
			
			if(args.length == 2)
				clientConnectionMaximum = Integer.parseInt(args[1]);
		
			ServerModifier.setClientConnectionMaximum(clientConnectionMaximum);
			Logger.appendColoredText("[client_connection_maximum set to "+clientConnectionMaximum+"]", Resources.DARK_GREEN);
		}
		else
		if(args[0].equals("play_remix")) {
			SoundPlayer.play("remix");
			
			Action action = new Action() {
				@Override
				public void execute() {
					NotificationUI.queueNotification("RAVE RIGHT", 11000, null, false);
					Logger.appendColoredText("/o/", Color.CYAN);
					Logger.appendColoredText("\\o/", Color.CYAN);
				}
			};
			
			NotificationUI.queueNotification("RAVE LEFT", 8300, action, false);
			Logger.appendColoredText("\\o\\", Color.CYAN);
			Logger.appendColoredText("\\o/", Color.CYAN);
		}
		else
		if(args[0].equals("display_popup")) {
			PopupUI.promptInput("POPUP TEST", false);
		}
		else
		if(args[0].equals("display_notification")) {
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
		if(args[0].equals("adapter_block")) {
			String display = "false";
			
			if(args.length == 2)
				display = args[1];
			
			Adapter.block(Boolean.parseBoolean(display));
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public static boolean isDeveloperModeEnabled() {
		return developerMode;
	}

}
