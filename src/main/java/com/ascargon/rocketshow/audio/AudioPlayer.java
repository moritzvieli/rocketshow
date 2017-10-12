package com.ascargon.rocketshow.audio;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.log4j.Logger;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);
	

	public void load() throws IOException {

	}

	public void setPositionInMillis(long position) {
		// TODO
	}

	public void play(String path) throws IOException {
	    try
	    {
	        //Clip clip = AudioSystem.getClip(info);
	        //clip.open(inputStream);
	        //lip.start();
	    }
	    catch (Exception e)
	    {
			// TODO
	    }
	}

	public void pause() throws IOException {
		// TODO
	}

	public void resume() throws IOException {
		// TODO
	}

	public void stop() throws IOException {
		// TODO
	}

}
