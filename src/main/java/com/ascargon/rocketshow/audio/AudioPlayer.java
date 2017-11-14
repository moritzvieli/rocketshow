package com.ascargon.rocketshow.audio;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);

	public enum PlayerType {
		ALSA_PLAYER, MPLAYER
	}

	private ShellManager shellManager;

	private String path;
	private String device;

	private PlayerType playerType = PlayerType.MPLAYER;

	public void load(PlayerType playerType, PlayerLoadedListener playerLoadedListener, String path, String device) throws IOException {
		this.playerType = playerType;
		this.path = path;
		this.device = device;

		if (playerType == PlayerType.MPLAYER) {
			shellManager = new ShellManager(new String[] { "mplayer", "-ao", "alsa:device=" + device, "-quiet",
					"-slave", "-cache-min", "99", path });

			// Pause, as soon as the song has been loaded and wait for it to be
			// played
			pause();

			// Wait for the player to get ready, because reading the input
			// stream in an infinite loop does not work properly (takes too much
			// resources and exiting the loop as soon as the player is loaded
			// breaks the process)
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						// Rewind to the start position
						shellManager.sendCommand("pausing seek 0 2", true);
					} catch (IOException e) {
					}

					playerLoadedListener.playerLoaded();
				}
			}, 3000 /* TODO Specify in global config */);
		} else if (playerType == PlayerType.ALSA_PLAYER) {
			playerLoadedListener.playerLoaded();
		}
	}

	public void play() throws IOException {
		if (playerType == PlayerType.MPLAYER) {
			shellManager.sendCommand("pause", true);
		} else if (playerType == PlayerType.ALSA_PLAYER) {
			shellManager = new ShellManager(new String[] { "aplay", "-D", "plug:" + device, path, "-i" });
		}
	}

	public void pause() throws IOException {
		if (playerType == PlayerType.MPLAYER) {
			shellManager.sendCommand("pause", true);
		}
	}

	public void resume() throws IOException {
		if (playerType == PlayerType.MPLAYER) {
			shellManager.sendCommand("pause", true);
		}
	}

	public void stop() throws Exception {
		if (shellManager != null) {
			if (playerType == PlayerType.MPLAYER) {
				shellManager.sendCommand("quit", true);
				shellManager.getProcess().waitFor();
				shellManager.close();
			} else if (playerType == PlayerType.ALSA_PLAYER) {
				shellManager.close();
			}
		}
	}

	public void close() throws Exception {
		stop();
	}

}
