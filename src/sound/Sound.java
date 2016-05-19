package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	private Clip clip;
	private AudioInputStream ais;
	private String name;
	private String filePath;
	
	public Sound(String name, String filePath) {
		this.name = name;
		this.filePath = filePath;
		
		try {
			ais = AudioSystem.getAudioInputStream(new File(filePath));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
	
	protected void setGain(float decibels) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(decibels);
	}
	
	protected String getFilePath() {
		return filePath;
	}
	
	protected String getName() {
		return name;
	}
	
	protected Clip getClip() {
		return clip;
	}
	
}