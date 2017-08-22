package com.ascargon.rocketshow.video;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.util.ShellManager;

public class VideoPlayer {

	final static Logger logger = Logger.getLogger(VideoPlayer.class);
	
	private long position;
	private ShellManager shellManager;

	public void load() throws IOException {
		shellManager = new ShellManager();
		
		// Cache the videoplayer to speedup following plays
		shellManager.sendCommand("omxplayer /opt/rocketshow/init.mp4");
	}

	public void setPositionInMillis(long position) {
		this.position = position;
	}

	public void play(String path) throws IOException {
		String startPos = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(position),
				TimeUnit.MILLISECONDS.toMinutes(position)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(position)),
				TimeUnit.MILLISECONDS.toSeconds(position)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));

		// TODO to make it more precise: Set position one second before and delay
		// the play by the remaining milliseconds
		shellManager.sendCommand("omxplayer --pos " + startPos + " " + path);
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

}
