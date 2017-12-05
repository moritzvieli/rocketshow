package com.ascargon.rocketshow.song.file;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.video.VideoPlayer;

public class VideoFile extends File {

	final static Logger logger = Logger.getLogger(VideoFile.class);
	
	public final static String VIDEO_PATH = "video/";
	
	private VideoPlayer videoPlayer;

	private Timer playTimer;
	
	@XmlTransient
	public String getPath() {
		return Manager.BASE_PATH + MEDIA_PATH + VIDEO_PATH + getName();
	}
	
	@Override
	public void load() throws Exception {
		logger.debug("Loading file '" + this.getName() + "...");
		
		this.setLoaded(false);
		this.setLoading(true);

		if(videoPlayer == null) {
			videoPlayer = new VideoPlayer();
		}
		videoPlayer.load(this, getPath());
	}

	@Override
	public void close() throws Exception {
		stop();
	}

	@Override
	public void play() throws IOException {
		String path = getPath();

		if (videoPlayer == null) {
			logger.error("Video player not initialized for file '" + getPath() + "'");
			return;
		}

		if (this.getOffsetMillis() > 0) {
			logger.debug("Wait " + this.getOffsetMillis() + " milliseconds before starting the video file '"
					+ this.getPath() + "'");

			playTimer = new Timer();
			playTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						playTimer = null;
						videoPlayer.play();
					} catch (IOException e) {
						logger.error("Could not play video video '" + path + "'", e);
					}
				}
			}, this.getOffsetMillis());
		} else {
			videoPlayer.play();
		}
	}

	@Override
	public void pause() throws IOException {
		if(playTimer != null) {
			playTimer.cancel();
			playTimer = null;
		}
		
		if (videoPlayer == null) {
			logger.error("Video player not initialized for file '" + getPath() + "'");
			return;
		}
		
		videoPlayer.pause();
	}

	@Override
	public void resume() throws IOException {
		if (videoPlayer == null) {
			logger.error("Video player not initialized for file '" + getPath() + "'");
			return;
		}
		
		videoPlayer.resume();
	}

	@Override
	public void stop() throws Exception {
		if(playTimer != null) {
			playTimer.cancel();
			playTimer = null;
		}
		
		if (videoPlayer == null) {
			logger.error("Video player not initialized for file '" + getPath() + "'");
			return;
		}
		
		this.setLoaded(false);
		this.setLoading(false);
		videoPlayer.stop();
	}

}
