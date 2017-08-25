package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;
import com.ascargon.rocketshow.song.file.MidiFile.MidiFileOutType;

public class MidiPlayer implements Receiver {

	final static Logger logger = Logger.getLogger(MidiPlayer.class);
	
	private Sequencer sequencer;
	
	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;
	
	private Midi2DmxMapping midi2DmxMapping;
	
	private Midi2DmxConverter midi2DmxConverter;
	
	private Manager manager;
	
	public MidiPlayer(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
		this.midi2DmxConverter = manager.getMidi2DmxConverter();
	}
	
	public void setPositionInMillis(long position) {
		sequencer.setMicrosecondPosition(position);
	}

	public void load(File file) throws Exception {
		if(sequencer != null) {
			if(sequencer.isOpen()) {
				sequencer.close();
			}
		}
		
		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();
		sequencer.getTransmitter().setReceiver(this);
		
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		sequencer.setSequence(is);
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
	}

	public void send(MidiMessage message, long timeStamp) {
		if (midiFileOutType == MidiFileOutType.DIRECT) {
			// Directly send the message to the out system
			try {
				javax.sound.midi.MidiDevice midiDevice = MidiUtil.getHardwareMidiDevice(manager.getSettings().getMidiOutDevice(), MidiDirection.OUT);
				
				if(midiDevice != null) {
					sequencer.getTransmitter().setReceiver(midiDevice.getReceiver());
				}
			} catch (MidiUnavailableException e) {
				logger.error(e.getStackTrace());
			}
		} else if(midiFileOutType == MidiFileOutType.DMX) {
			// Map the midi to DMX out
			try {
				midi2DmxConverter.processMidiEvent(message, timeStamp, midi2DmxMapping);
			} catch (IOException e) {
				logger.error("Could not send DMX signal from MIDI file", e);
			}
		}
	}

	public MidiFileOutType getMidiFileOutType() {
		return midiFileOutType;
	}

	public void setMidiFileOutType(MidiFileOutType midiFileOutType) {
		this.midiFileOutType = midiFileOutType;
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

}
