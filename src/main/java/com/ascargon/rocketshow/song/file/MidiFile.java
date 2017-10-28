package com.ascargon.rocketshow.song.file;

import java.io.File;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.midi.MidiPlayer;
import com.ascargon.rocketshow.midi.MidiRouting;

public class MidiFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(MidiFile.class);

	private MidiPlayer midiPlayer;

	private MidiRouting midiRouting;

	public MidiFile() {
		setMidiRouting(new MidiRouting());
	}
	
	public void load() throws Exception {
		midiPlayer = new MidiPlayer(this.getManager(), midiRouting);
		midiPlayer.load(new File(this.getPath()));
	}

	@Override
	public void play() {
		if (this.getOffsetInMillis() >= 0) {
			logger.debug("Wait " + this.getOffsetInMillis() + " milliseconds before starting the midi file");

			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					midiPlayer.play();
				}
			}, this.getOffsetInMillis());
		} else {
			logger.debug("Set MIDI file offset to " + (this.getOffsetInMillis() * -1000) + " milliseconds");

			midiPlayer.setPositionInMillis(this.getOffsetInMillis() * -1000);
			midiPlayer.play();
		}
	}

	@Override
	public void pause() {
		midiPlayer.pause();
	}

	@Override
	public void resume() {
		midiPlayer.play();
	}

	@Override
	public void stop() throws Exception {
		midiPlayer.stop();
	}

	@Override
	public void close() {
		midiPlayer.close();
	}

	public void setMidiRouting(MidiRouting midiRouting) {
		midiRouting.setMidiSource("MIDI file '" + getPath() + "'");
		this.midiRouting = midiRouting;
	}

	public MidiRouting getMidiRouting() {
		return midiRouting;
	}

}
