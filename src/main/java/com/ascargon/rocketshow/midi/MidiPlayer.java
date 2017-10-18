package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.file.MidiFile.MidiFileOutType;

public class MidiPlayer implements MidiDeviceConnectedListener {

	final static Logger logger = Logger.getLogger(MidiPlayer.class);

	private Sequencer sequencer;
	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;
	private Midi2DmxReceiver midi2DmxReceiver;
	private Manager manager;
	private javax.sound.midi.MidiDevice midiSender;

	public MidiPlayer(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
		midi2DmxReceiver = new Midi2DmxReceiver(manager);

		manager.addMidiOutDeviceConnectedListener(this);
	}

	@Override
	public void deviceConnected(javax.sound.midi.MidiDevice midiDevice) {
		// Connect the sequencer to the MIDI output device
		try {
			sequencer.getTransmitter().setReceiver(midiDevice.getReceiver());
		} catch (MidiUnavailableException e) {
			logger.error("Could not set MIDI out device as sender for the file player", e);
		}
	}

	@Override
	public void deviceDisconnected(javax.sound.midi.MidiDevice midiDevice) {
		// Nothing to do at the moment
	}

	public void setPositionInMillis(long position) {
		sequencer.setMicrosecondPosition(position);
	}

	public void load(File file) throws Exception {
		if (sequencer != null) {
			if (sequencer.isOpen()) {
				sequencer.close();
			}
		}

		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();

		InputStream is = new BufferedInputStream(new FileInputStream(file));
		sequencer.setSequence(is);

		if (midiFileOutType == MidiFileOutType.DMX) {
			// Connect the sequencer to the this receiver for DMX mapping
			sequencer.getTransmitter().setReceiver(midi2DmxReceiver);
		}
	}

	public void play() {
		logger.debug("Starting sequencer from position " + sequencer.getMicrosecondPosition());
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
		midi2DmxReceiver.close();
		manager.removeMidiOutDeviceConnectedListener(this);

		if (midiSender != null && midiSender.isOpen()) {
			midiSender.close();
		}
	}

	public MidiFileOutType getMidiFileOutType() {
		return midiFileOutType;
	}

	public void setMidiFileOutType(MidiFileOutType midiFileOutType) {
		this.midiFileOutType = midiFileOutType;
	}

	public Midi2DmxReceiver getMidi2DmxReceiver() {
		return midi2DmxReceiver;
	}

	public void setMidi2DmxReceiver(Midi2DmxReceiver midi2DmxReceiver) {
		this.midi2DmxReceiver = midi2DmxReceiver;
	}

}
