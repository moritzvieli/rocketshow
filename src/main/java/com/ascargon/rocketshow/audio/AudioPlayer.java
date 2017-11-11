package com.ascargon.rocketshow.audio;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);
	
	private ShellManager shellManager;

	public void load(PlayerLoadedListener playerLoadedListener, String path, String device) throws IOException {
		shellManager = new ShellManager();
		shellManager.sendCommand("mplayer -ao alsa:device=" + device + " " + path);
		
		// Send a space to stop playing, as soon as the song has been loaded
		shellManager.sendCommand("p");
	}

	public void play() throws IOException {
		shellManager.sendCommand("p");
	}

	public void pause() throws IOException {
		shellManager.sendCommand("p");
	}

	public void resume() throws IOException {
		shellManager.sendCommand("p");
	}

	public void stop() throws IOException {
		shellManager.sendCommand("q");
	}

	public void close() {
		if(shellManager != null) {
			shellManager.close();
		}
	}
	
}
