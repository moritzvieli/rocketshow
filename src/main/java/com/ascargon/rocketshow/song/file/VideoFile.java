package com.ascargon.rocketshow.song.file;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.video.VideoPlayer;

public class VideoFile extends File {

	final static Logger logger = Logger.getLogger(VideoFile.class);
	
	public final static String VIDEO_PATH = "video/";
	
	private VideoPlayer videoPlayer;

	private String getPath() {
		return Manager.BASE_PATH + MEDIA_PATH + VIDEO_PATH + getName();
	}
	
	@Override
	public void load() throws IOException {
		logger.debug("Load file '" + this.getName());
		
		this.setLoaded(false);
		this.setLoading(true);

		videoPlayer = new VideoPlayer();
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

		if (this.getOffsetInMillis() > 0) {
			logger.debug("Wait " + this.getOffsetInMillis() + " milliseconds before starting the video file '"
					+ this.getPath() + "'");

			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						videoPlayer.play();
					} catch (IOException e) {
						logger.error("Could not play video video \"" + path + "\"");
						logger.error(e.getStackTrace());
					}
				}
			}, this.getOffsetInMillis());
		} else {
			videoPlayer.play();
		}
	}

	@Override
	public void pause() throws IOException {
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
		if (videoPlayer == null) {
			logger.error("Video player not initialized for file '" + getPath() + "'");
			return;
		}
		
		this.setLoaded(false);
		videoPlayer.stop();
	}

}
