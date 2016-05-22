package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Class models a sound.
 * 
 * @author kieransherman
 *
 */
public class Sound {
	
	private Clip clip;
	private AudioInputStream ais;
	private String name;
	
	/**
	 * Create a new sound at a filepath and assign it a name.
	 * 
	 * @param name the sound's name.
	 * @param filePath the fielpath to the sound.
	 */
	public Sound(String name, String filePath) {
		this.name = name;
		
		try {
			ais = AudioSystem.getAudioInputStream(new File(filePath));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play the sound.
	 */
	protected void play() {
		try {
			if(clip != null) {
				clip.setFramePosition(0);
				clip.start();
				clip.drain();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loop the sound.
	 */
	protected void loop() {
		try {
			if(clip != null) {
				clip.setFramePosition(0);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.drain();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the gain.
	 */
	protected void setGain(float decibels) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(decibels);
	}
	
	
	/**
	 * return the name.
	 */
	protected String getName() {
		return name;
	}
	
}