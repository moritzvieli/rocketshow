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
		shellManager = new ShellManager(new String[] { "mplayer", "-ao", "alsa:device=" + device, path });

		// Pause, as soon as the song has been loaded and wait for it to be
		// played
		pause();

		new Thread(new Runnable() {
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(shellManager.getInputStream()));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						logger.trace("Output from audio player: " + line);
						
						if (line.contains("=====  PAUSE  =====")) {
							logger.debug("Audio player loaded");
							playerLoadedListener.playerLoaded();
						}
					}
				} catch (Exception e) {} finally {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("Could not close stream reader for video player process", e);
					}
					;
				}

			}
		}).start();
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

	public void stop() throws Exception {
		if (shellManager != null) {
			shellManager.sendCommand("q");
			shellManager.getProcess().waitFor();
			shellManager.close();
		}
	}

	public void close() throws Exception {
		stop();
	}

}
