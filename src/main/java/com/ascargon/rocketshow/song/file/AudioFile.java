package com.ascargon.rocketshow.song.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.audio.AudioPlayer;

public class AudioFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(AudioFile.class);

	private AudioPlayer audioPlayer;

	@Override
	public void load() {
		audioPlayer = new AudioPlayer();
	}

	@Override
	public void close() {
		if (audioPlayer != null) {
			audioPlayer.close();
		}
	}

	@Override
	public void play() throws Exception {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + this.getPath() + "'");
			return;
		}

		audioPlayer.play(new File(this.getPath()));
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
