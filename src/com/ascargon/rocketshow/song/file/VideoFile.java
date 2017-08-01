package com.ascargon.rocketshow.song.file;

import com.ascargon.rocketshow.video.VideoPlayer;

public class VideoFile extends File {

	@Override
	public void play() {
		VideoPlayer videoPlayer = this.getManager().getVideoPlayer();
		
		if (this.getOffsetInMillis() >= 0) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					videoPlayer.play();
				}
			}, this.getOffsetInMillis());
		} else {
			videoPlayer.setPositionInMillis(this.getOffsetInMillis() * -1);
			videoPlayer.play();
		}
	}

	@Override
	public void load() {
		this.getManager().getVideoPlayer().load(this.getPath());
	}

	@Override
	public void pause() {
		this.getManager().getVideoPlayer().pause();
	}

}
