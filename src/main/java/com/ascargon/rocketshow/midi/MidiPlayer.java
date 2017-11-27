package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.file.PlayerLoadedListener;

public class MidiPlayer {

	final static Logger logger = Logger.getLogger(MidiPlayer.class);

	private Sequencer sequencer;
	private MidiRouting midiRouting;

	public MidiPlayer(Manager manager, MidiRouting midiRouting) throws MidiUnavailableException {
		this.midiRouting = midiRouting;
	}

	public void setPositionInMillis(long position) {
		sequencer.setMicrosecondPosition(position);
	}

	public void load(PlayerLoadedListener playerLoadedListener, String path) throws Exception {
		if (sequencer != null) {
			if (sequencer.isOpen()) {
				sequencer.close();
			}
		}

		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();

		InputStream is = new BufferedInputStream(new FileInputStream(new File(path)));
		sequencer.setSequence(is);

		midiRouting.setTransmitter(sequencer.getTransmitter());

		// Read the first bytes
		sequencer.start();
		sequencer.stop();

		playerLoadedListener.playerLoaded();
	}

	public static long getDuration(String path) throws Exception {
		long duration;

		Sequence sequence = MidiSystem.getSequence(new File(path));
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		sequencer.setSequence(sequence);
		duration = sequencer.getMicrosecondLength() / 1000;
		sequencer.close();

		return duration;
	}

	public void play() {
		logger.debug("Starting MIDI player from position " + sequencer.getMicrosecondPosition());
		sequencer.start();
	}

	public void pause() {
		sequencer.stop();
	}

	public void stop() {
		sequencer.stop();
		sequencer.setMicrosecondPosition(0);
	}

	public void close() {
		sequencer.close();
		midiRouting.close();
	}

}
