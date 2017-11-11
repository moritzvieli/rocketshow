package com.ascargon.rocketshow.song.file;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.midi.MidiPlayer;
import com.ascargon.rocketshow.midi.MidiRouting;

public class MidiFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(MidiFile.class);

	public final static String MIDI_PATH = "midi/";
	
	private MidiPlayer midiPlayer;

	private MidiRouting midiRouting;
	
	public MidiFile() {
		setMidiRouting(new MidiRouting());
	}
	
	private String getPath() {
		return Manager.BASE_PATH + MEDIA_PATH + MIDI_PATH + getName();
	}

	public void load() throws Exception {
		this.setLoaded(false);
		
		midiPlayer = new MidiPlayer(this.getManager(), midiRouting);
		midiPlayer.load(this, this.getPath());
	}

	@Override
	public void play() {
		if (midiPlayer == null) {
			logger.error("MIDI player not initialized for file '" + getPath() + "'");
			return;
		}

		if (this.getOffsetInMillis() > 0) {
			logger.debug("Wait " + this.getOffsetInMillis() + " milliseconds before starting the MIDI file '" + this.getPath() + "'");
			
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					midiPlayer.play();
				}
			}, this.getOffsetInMillis());
		} else {
			midiPlayer.play();
		}
	}

	@Override
	public void pause() {
		if (midiPlayer == null) {
			logger.error("MIDI player not initialized for file '" + getPath() + "'");
			return;
		}

		midiPlayer.pause();
	}

	@Override
	public void resume() {
		if (midiPlayer == null) {
			logger.error("MIDI player not initialized for file '" + getPath() + "'");
			return;
		}

		midiPlayer.play();
	}

	@Override
	public void stop() throws Exception {
		if (midiPlayer == null) {
			logger.error("MIDI player not initialized for file '" + getPath() + "'");
			return;
		}

		midiPlayer.stop();
	}

	@Override
	public void close() {
		if (midiPlayer != null) {
			midiPlayer.close();
		}
	}

	public void setMidiRouting(MidiRouting midiRouting) {
		midiRouting.setMidiSource("MIDI file '" + getPath() + "'");
		this.midiRouting = midiRouting;
	}

	public MidiRouting getMidiRouting() {
		return midiRouting;
	}
	
}
