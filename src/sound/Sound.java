package sound;

import java.io.File;

import javax.sound.sampled.*;

public class Sound {
	
	private Clip clip;
	
	public static Sound background1 = new Sound("src/files/sounds/background1.wav");
	public static Sound notification = new Sound("src/files/sounds/notification.wav");
	public static Sound computerHum = new Sound("src/files/sounds/hum.wav");
	public static Sound computerStartup = new Sound("src/files/sounds/startup.wav");
	public static Sound computerHardDrive = new Sound("src/files/sounds/hard-drive.wav");
	public static Sound[] keyboardKeys = {
			new Sound("src/files/sounds/keyboard/key1.wav"),
			new Sound("src/files/sounds/keyboard/key2.wav"),
			new Sound("src/files/sounds/keyboard/key3.wav"),
			new Sound("src/files/sounds/keyboard/key4.wav"),
			new Sound("src/files/sounds/keyboard/key5.wav"),
			new Sound("src/files/sounds/keyboard/key6.wav"),
			new Sound("src/files/sounds/keyboard/key7.wav"),
			new Sound("src/files/sounds/keyboard/key8.wav"),
			new Sound("src/files/sounds/keyboard/key9.wav"),
			new Sound("src/files/sounds/keyboard/key10.wav"),
	};
	
	public Sound (String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setGain(float decibels) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(decibels);
	}

	public void play() {
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.start();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		if(clip == null) return;
		clip.stop();
	}
	
	public void loop() {
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isActive(){
		return clip.isActive();
	}
}