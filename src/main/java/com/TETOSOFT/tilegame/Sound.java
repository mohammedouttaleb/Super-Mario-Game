package com.TETOSOFT.tilegame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Handles play, pause, and looping of sounds for the game.
 */
public class Sound {
	AudioInputStream audioInputStream;
	private Clip myClip;
	public Clip getMyClip() {
		return myClip;
	}
	public void setMyClip(Clip myClip) {
		this.myClip = myClip;
	}
	public Sound(String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {	{
				// create AudioInputStream object
				audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());

				// create clip reference
				myClip = AudioSystem.getClip();

				// open audioInputStream to the clip
				myClip.open(audioInputStream);

			}

			}
			else {
				throw new RuntimeException("Sound: file not found: " + fileName);
			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Sound: Malformed URL: " + e);
		}
		catch (UnsupportedAudioFileException e) {
			throw new RuntimeException("Sound: Unsupported Audio File: " + e);
		}
		catch (IOException e) {
			throw new RuntimeException("Sound: Input/Output Error: " + e);
		}
		catch (LineUnavailableException e) {
			throw new RuntimeException("Sound: Line Unavailable: " + e);
		}
	}
	public void play(){
		myClip.setFramePosition(0);  // Must always rewind!
		myClip.loop(0);
		myClip.start();
	}
	public void loop(){
		myClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop(){
		myClip.stop();
	}
}