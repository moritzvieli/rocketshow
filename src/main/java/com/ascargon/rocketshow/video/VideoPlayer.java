package com.ascargon.rocketshow.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class VideoPlayer {

	final static Logger logger = Logger.getLogger(VideoPlayer.class);

	private ShellManager shellManager;

	public void load(PlayerLoadedListener playerLoadedListener, String path) throws IOException {
		shellManager = new ShellManager();

		new Thread(new Runnable() {
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(shellManager.getInputStream()));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						// TODO playerLoadedListener.playerLoaded();
						// builder.redirectErrorStream(true);??
						logger.info("Line received:" + line);
					}
				} catch (Exception e) {
					logger.error("Could not wait for the video player to get loaded", e);
				}

			}
		}).start();

		shellManager.sendCommand("omxplayer " + path);
	}

	public void play() throws IOException {
		shellManager.sendCommand("p");
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
		if (shellManager != null) {
			shellManager.close();
		}
	}

}
