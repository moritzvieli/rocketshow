package com.ascargon.rocketshow.video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.file.PlayerLoadedListener;
import com.ascargon.rocketshow.util.ShellManager;

public class VideoPlayer {

	final static Logger logger = Logger.getLogger(VideoPlayer.class);

	private ShellManager shellManager;

	private Timer loadTimer;
	private Timer closeTimer;
	private boolean closing;
	private boolean loop;

	public void load(PlayerLoadedListener playerLoadedListener, String path) throws IOException, InterruptedException {
		logger.debug("Loading video '" + path + "'");

		List<String> params = new ArrayList<String>();
		params.add("omxplayer");
		params.add(path);

		// Adjust framerate/resolution to video
		params.add("-r");

		// Set background to black
		params.add("-b");

		if (loop) {
			params.add("--loop");
		}

		shellManager = new ShellManager(params.toArray(new String[0]));

        pause();
		
		// Wait for the player to get ready, because reading the input stream in
		// an infinite loop does not work properly (takes too much resources and
		// exiting the loop as soon as the player is loaded breaks the process)
		loadTimer = new Timer();
		loadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				loadTimer.cancel();
				loadTimer = null;

				if (playerLoadedListener != null) {
					playerLoadedListener.playerLoaded();
				}
			}
		}, 1000 /* TODO Specify in global config */);
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
		closing = true;

		if (loadTimer != null) {
			loadTimer.cancel();
			loadTimer = null;
		}

		// Delay a close as backup, because fast load/short may sometimes fail
		// TODO Retry, until closed
		closeTimer = new Timer();
		closeTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				closeTimer.cancel();
				closeTimer = null;

				if (closing) {
					try {
						shellManager.sendCommand("q");
					} catch (IOException e) {
					}
				}
			}
		}, 500);

		if (shellManager != null) {
			shellManager.sendCommand("q");
			shellManager.getProcess().destroyForcibly();
			shellManager.getProcess().waitFor();
			shellManager.close();

		}

		if (closeTimer != null) {
			closeTimer.cancel();
			closeTimer = null;
		}

		closing = false;
	}

	public void close() throws Exception {
		stop();
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

}
