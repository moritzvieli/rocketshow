package com.ascargon.rocketshow.audio;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);

	private ShellManager shellManager;

	public void load(PlayerLoadedListener playerLoadedListener, String path, String device) throws IOException {
		shellManager = new ShellManager(
				new String[] { "mplayer", "-ao", "alsa:device=" + device, "-quiet", "-slave", path });

		// Pause, as soon as the song has been loaded and wait for it to be
		// played
		pause();

		// Wait for the player to get ready, because reading the input stream in
		// an infinite loop does not work properly (takes too much resources and
		// exiting the loop as soon as the player is loaded breaks the process)
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				playerLoadedListener.playerLoaded();
			}
		}, 3000 /* TODO Specify in global config */);
	}

	public void play() throws IOException {
		shellManager.sendCommand("pause", true);
	}

	public void pause() throws IOException {
		shellManager.sendCommand("pause", true);
	}

	public void resume() throws IOException {
		shellManager.sendCommand("pause", true);
	}

	public void stop() throws Exception {
		if (shellManager != null) {
			shellManager.sendCommand("q", true);
			shellManager.getProcess().waitFor();
			shellManager.close();
		}
	}

	public void close() throws Exception {
		stop();
	}

}
