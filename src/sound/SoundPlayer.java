package sound;

import java.util.ArrayList;

import util.Resources;

public class SoundPlayer {
	
	protected static ArrayList<Sound> allSounds;
	
	private SoundPlayer() {}
	
	static {
		allSounds = new ArrayList<Sound>();
		
		String [] soundData = Resources.readText(Resources.DIRECTORY+"src/files/reference/sound.txt");
		String [] data;
		String name, filePath, gain;
		Sound sound;
		
		for(String str : soundData) {
			if(str == null || str.equals(""))
				continue;
			
			str = str.substring("new Sound(".length(), str.length()-");".length());
			data = str.split(",");
			name = data[0].substring(1, data[0].length()-1);
			filePath = data[1].substring(2, data[1].length()-1);
			gain = "0";
			
			if(data.length > 2)
				gain = data[2].substring(1);
			
			sound = new Sound(name, Resources.DIRECTORY+filePath);
			sound.setGain(Float.parseFloat(gain));
			allSounds.add(sound);
		}
	}
	
	public static void play(String soundName) {
		int index = getIndex(soundName);
		
		if(index != -1)
			allSounds.get(index).play();
	}
	
	public static void loop(String soundName) {
		int index = getIndex(soundName);
		
		if(index != -1)
			allSounds.get(index).loop();
	}
	
	private static int getIndex(String soundName) {
		for(int i = 0; i < allSounds.size(); i++)
			if(allSounds.get(i).getName().equals(soundName))
				return i;
		
		return -1;
	}

}
