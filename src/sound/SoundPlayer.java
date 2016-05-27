package sound;

import java.util.ArrayList;

import util.Resources;

/**
 * Class consists exclusively of static methods that modfiy sound operations.
 * 
 * @author kieransherman
 */
public class SoundPlayer {
	
	protected static ArrayList<Sound> allSounds;
	private static boolean mute;
	
	// Prevent object instantiation
	private SoundPlayer() {}
	
	static {
		allSounds = new ArrayList<Sound>();
		
		String [] soundData = Resources.parseTextFromFile(Resources.SOUND, "\n");
		
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
	
	/**
	 * Play the sound registered with the sound's name.
	 * 
	 * @param soundName the sound name.
	 */
	public static void play(String soundName) {
		if(mute)
			return;
		
		int index = getIndex(soundName);
		
		if(index != -1)
			allSounds.get(index).play();
	}
	
	/**
	 * Loop the sound registered with the sound's name.
	 * 
	 * @param soundName the sound name.
	 */
	public static void loop(String soundName) {
		if(mute)
			return;
		
		int index = getIndex(soundName);
		
		if(index != -1)
			allSounds.get(index).loop();
	}
	
	/**
	 * Sets whether sounds will be muted or not.
	 * 
	 * @param mute whether or not to mute sounds.
	 */
	public static void setMuted(boolean mute) {
		SoundPlayer.mute = mute;
	}
	
	/**
	 * Return the index of a sound if registered.
	 */
	private static int getIndex(String soundName) {
		for(int i = 0; i < allSounds.size(); i++)
			if(allSounds.get(i).getName().equals(soundName))
				return i;
		
		return -1;
	}

}
