package com.ascargon.rocketshow.song.file;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.video.VideoPlayer;

public class VideoFile extends File {

	final static Logger logger = Logger.getLogger(VideoFile.class);
	
	@Override
	public void load() throws IOException {}
	
	@Override
	public void play() throws IOException {
		VideoPlayer videoPlayer = this.getManager().getVideoPlayer();
		String path = this.getPath();

		if (this.getOffsetInMillis() >= 0) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						videoPlayer.play(path);
					} catch (IOException e) {
						logger.error("Could not play video \"" + path + "\"");
						logger.error(e.getStackTrace());
					}
				}
			}, this.getOffsetInMillis());
		} else {
			videoPlayer.setPositionInMillis(this.getOffsetInMillis() * -1);
			videoPlayer.play(path);
		}
	}

	@Override
	public void pause() throws IOException {
		this.getManager().getVideoPlayer().pause();
	}
	
	@Override
	public void resume() throws IOException {
		this.getManager().getVideoPlayer().resume();
	}

	@Override
	public void stop() throws Exception {
		this.getManager().getVideoPlayer().stop();
	}
	
}
