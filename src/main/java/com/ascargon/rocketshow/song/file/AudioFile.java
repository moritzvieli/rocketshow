package com.ascargon.rocketshow.song.file;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.audio.AudioPlayer;

public class AudioFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(AudioFile.class);

	private AudioPlayer audioPlayer;

	@Override
	public void load() throws IOException {
		this.setLoaded(false);
		
		audioPlayer = new AudioPlayer();
		
		// TODO Device
		audioPlayer.load(this, this.getPath(), "stereo1");
	}

	@Override
	public void close() {
		if(audioPlayer != null) {
			audioPlayer.close();
		}
	}

	@Override
	public void play() throws Exception {
		String path = this.getPath();
		
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + this.getPath() + "'");
			return;
		}

		if (this.getOffsetInMillis() > 0) {
			logger.debug("Wait " + this.getOffsetInMillis() + " milliseconds before starting the audio file '" + this.getPath() + "'");
			
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						audioPlayer.play();
					} catch (IOException e) {
						logger.error("Could not play audio file \"" + path + "\"");
						logger.error(e.getStackTrace());
					}
				}
			}, this.getOffsetInMillis());
		} else {
			audioPlayer.play();
		}
	}

	@Override
	public void pause() throws IOException {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + this.getPath() + "'");
			return;
		}

		audioPlayer.pause();
	}

	@Override
	public void resume() throws IOException {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + this.getPath() + "'");
			return;
		}

		audioPlayer.resume();
	}

	@Override
	public void stop() throws Exception {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + this.getPath() + "'");
			return;
		}

		audioPlayer.stop();
	}

}
