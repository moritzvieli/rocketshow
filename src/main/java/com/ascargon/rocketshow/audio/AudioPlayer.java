package com.ascargon.rocketshow.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);
	
	private ShellManager shellManager;

	public void load(PlayerLoadedListener playerLoadedListener, String path, String device) throws IOException {
		shellManager = new ShellManager();
		shellManager.sendCommand("mplayer -ao alsa:device=" + device + " " + path);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Pause, as soon as the song has been loaded and wait for it to be played
		pause();
		
		new Thread(new Runnable() {
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(shellManager.getInputStream()));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						// TODO playerLoadedListener.playerLoaded();
						// builder.redirectErrorStream(true);??
						logger.info("AUDIOPLAYER Line received:" + line);
						
						if(line.contains("=====  PAUSE  =====")) {
							logger.debug("Audio player loaded");
							playerLoadedListener.playerLoaded();
							break;
						}
					}
				} catch (Exception e) {
					logger.error("Could not wait for the video player to get loaded", e);
				}

			}
		}).start();
	}

	public void play() throws IOException {
		shellManager.sendCommand("p", false);
	}

	public void pause() throws IOException {
		shellManager.sendCommand("p", false);
	}

	public void resume() throws IOException {
		shellManager.sendCommand("p", false);
	}

	public void stop() throws IOException {
		shellManager.sendCommand("q", false);
	}

	public void close() {
		if(shellManager != null) {
			shellManager.close();
		}
	}
	
}
