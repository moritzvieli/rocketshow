package com.ascargon.rocketshow.song.file;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.audio.AudioPlayer;

public class AudioFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(AudioFile.class);

	public final static String AUDIO_PATH = "audio/";
	
	private AudioPlayer audioPlayer;
	
	private String device;

	private String getPath() {
		return Manager.BASE_PATH + MEDIA_PATH + AUDIO_PATH + getName();
	}
	
	@Override
	public void load() throws IOException {
		logger.debug("Load file '" + this.getName());
		
		this.setLoaded(false);
		this.setLoading(true);
		
		audioPlayer = new AudioPlayer();
		
		audioPlayer.load(this.getManager().getSettings().getAudioPlayerType(), this, getPath(), device);
	}

	@Override
	public void close() throws Exception {
		stop();
	}

	@Override
	public void play() throws Exception {
		String path = getPath();
		
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + path + "'");
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
			logger.error("Audio player not initialized for file '" + getPath() + "'");
			return;
		}

		audioPlayer.pause();
	}

	@Override
	public void resume() throws IOException {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + getPath() + "'");
			return;
		}

		audioPlayer.resume();
	}

	@Override
	public void stop() throws Exception {
		if (audioPlayer == null) {
			logger.error("Audio player not initialized for file '" + getPath() + "'");
			return;
		}

		this.setLoaded(false);
		audioPlayer.stop();
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
