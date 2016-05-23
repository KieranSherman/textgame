package main.misc;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.ui.Window;
import main.ui.components.display.DisplayUI;
import main.ui.components.display.notification.NotificationUI;
import main.ui.components.popup.PopupUI;
import network.Adapter;
import network.packet.types.Packet03Message;
import network.server.ServerModifier;
import sound.SoundPlayer;
import util.Action;
import util.Resources;
import util.StringFormatter;
import util.out.DefaultLogger;

/**
 * This class consists exclusively of static methods that handle commands.
 * 
 * @author kieransherman
 * @see #parseCommand(String)
 *
 */
public class Developer {
	
	private static boolean developerMode = false;
	
	// Prevent object instantiation
	private Developer() {}
	
	/**
	 * Determines if {@code str} is a command.  If so, the appropriate command is executed.
	 * Otherwise, an error is displayed.
	 * 
	 * @param str the command.
	 */
	public static void parseCommand(String str) {
		if(str == null)
			return;

		str = str.toLowerCase();
		String [] args = str.split("\\s+");
		
		if(!Developer.userCommand(args) && !Developer.developerCommand(args)) {
			DefaultLogger.appendColoredText("[unrecognized command]", Color.RED);
			SoundPlayer.play("error");
		}

	}
	

	/**
	 * Returns whether or not the String[] is a user command.
	 */
	private static boolean userCommand(String[] args) {
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
			DefaultLogger.appendColoredText("[sent notes]", Color.GRAY);
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
				DefaultLogger.appendColoredText("[developer commands enabled]", Resources.DARK_GREEN);
			else
				DefaultLogger.appendColoredText("[developer commands disabled]", Resources.DARK_GREEN);
			
			return true;
		}
		else {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns whether or not the String[] is a developer command. 
	 */
	private static boolean developerCommand(String[] args) {
		if(!developerMode)
			return false;
		
			if(args[0].equals("local_host_maximum")) {
				int localHostMaximum = 1;
				
				if(args.length == 2)
					localHostMaximum = Integer.parseInt(args[1]);
			
				ServerModifier.setLocalHostMaximum(localHostMaximum);
				DefaultLogger.appendColoredText("[local_host_maximum set to "+localHostMaximum+"]", Resources.DARK_GREEN);
			}
		else
			if(args[0].equals("same_client_maximum")) {
				int sameClientMaximum = 1;
				
				if(args.length == 2)
					sameClientMaximum = Integer.parseInt(args[1]);
			
				ServerModifier.setSameClientMaximum(sameClientMaximum);
				DefaultLogger.appendColoredText("[same_client_maximum set to "+sameClientMaximum+"]", Resources.DARK_GREEN);
			}
		else
			if(args[0].equals("client_connection_maximum")) {
				int clientConnectionMaximum = 1;
				
				if(args.length == 2)
					clientConnectionMaximum = Integer.parseInt(args[1]);
			
				ServerModifier.setClientConnectionMaximum(clientConnectionMaximum);
				DefaultLogger.appendColoredText("[client_connection_maximum set to "+clientConnectionMaximum+"]", Resources.DARK_GREEN);
			}
		else
			if(args[0].equals("play_remix")) {
				SoundPlayer.play("remix");
				
				Action action = new Action() {
					@Override
					public void execute() {
						NotificationUI.queueNotification("RAVE RIGHT", 11000, null, false);
						DefaultLogger.appendColoredText("/o/", Color.CYAN);
						DefaultLogger.appendColoredText("\\o/", Color.CYAN);
					}
				};
				
				NotificationUI.queueNotification("RAVE LEFT", 8300, action, false);
				DefaultLogger.appendColoredText("\\o\\", Color.CYAN);
				DefaultLogger.appendColoredText("\\o/", Color.CYAN);
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
		else
			if(args[0].equals("remove_upnp_map_at_port")) {
				int port = 9999;
				
				if(args.length == 2)
					port = Integer.parseInt(args[1]);
				
				ServerModifier.removeUPnPMapAtPort(port);
				DefaultLogger.appendColoredText("[removed UPnP map at "+port+"]", Color.RED);
			}
		else
			if(args[0].equals("add_upnp_map_at_port")) {
				int port = 9999;
				
				if(args.length == 2)
					port = Integer.parseInt(args[1]);
				
				ServerModifier.addUPnPMapAtPort(port);
				DefaultLogger.appendColoredText("[added UPnP map at "+port+"]", Resources.DARK_GREEN);
			}
		else
			if(args[0].equals("set_upnp_remap")) {
				String remap = "false";
				
				if(args.length == 2)
					remap = args[1];
				
				ServerModifier.setUPnPRemap(Boolean.parseBoolean(remap));
				DefaultLogger.appendColoredText("[set UPnP remap to "+remap+"]", Resources.DARK_GREEN);
			}
		else
			if(args[0].equals("file_bug_report")) {
				PopupUI.promptReport("FILE BUG", "NAME", "DESCRIPTION", 15);
				
				if(PopupUI.getData() == null)
					return true;
				
				String bugName = (String)PopupUI.getData()[0];
				String description = (String)PopupUI.getData()[1];
				String version = (String)PopupUI.getData()[2];
				String format = "%-16s\t%-8s\t\t%-8s\t\t\t%-50s\t\t%-12s\t%-8s\t\t%-8s\n";
				
				ArrayList<String> descriptionSplit = StringFormatter.getWordWrap(description, 50);
				
				StringFormatter sf = new StringFormatter(format);
				sf.addArgument(bugName);
				sf.addArgument(new SimpleDateFormat("MM/dd/yy").format(new Date()));
				sf.addArgument(version);
				sf.addArgument(descriptionSplit.get(0));
				sf.addArgument("UNRESOLVED");
				sf.addArgument("n/a");
				sf.addArgument("n/a");
				
				for(int i = 1; i < descriptionSplit.size(); i++)
					sf.addArgument(descriptionSplit.get(i), 3, 3);
				
				Resources.writeTextToFile("src/files/reference/lists/buglist.txt", sf.getFormattedString()+"\n", true);
				
				System.out.println(sf.getFormattedString());
				DefaultLogger.appendColoredText("[bug filed: "+bugName+"]", Resources.DARK_GREEN);
 			}
		else {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns whether or not developer mode is enabled.
	 * 
	 * @return the status of the developer mode.
	 */
	public static boolean isDeveloperModeEnabled() {
		return developerMode;
	}

}
