package com.ascargon.rocketshow.video;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.ascargon.rocketshow.util.ShellManager;

public class VideoPlayer {

	private String path;
	private long position;
	private ShellManager shellManager;

	public void load(String path) {
		this.path = path;
	}

	public void setPositionInMillis(long position) {
		this.position = position;
	}

	public void play() {
		shellManager = new ShellManager();
		
		String startPos = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(position),
				TimeUnit.MILLISECONDS.toMinutes(position)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(position)),
				TimeUnit.MILLISECONDS.toSeconds(position)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));

		// TODO to make it more exact: Set position one second before and delay
		// the play by the remaining milliseconds

		shellManager.sendCommand("omxplayer --pos " + startPos + " " + path);
	}

	public void pause() {
		shellManager.sendCommand("p", false);
	}

	public void resume() {
		shellManager.sendCommand("p", false);
	}

	public void stop() {
		shellManager.sendCommand("q", false);
	}

}
