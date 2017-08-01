package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.song.file.MidiFile.MidiFileOutType;

public class MidiPlayer implements Receiver {

	private Sequencer sequencer;
	
	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;
	
	private Midi2DmxMapping midi2DmxMapping;
	
	private Midi2DmxConverter midi2DmxConverter;
	
	public MidiPlayer(Manager manager) throws MidiUnavailableException {
		this.midi2DmxConverter = manager.getMidi2DmxConverter();
		
		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();
		sequencer.getTransmitter().setReceiver(this);
	}
	
	public void setPositionInMillis(long position) {
		sequencer.setMicrosecondPosition(position);
	}

	public void load(File file) throws Exception {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		sequencer.setSequence(is);
	}

	public void play() {
		sequencer.start();
	}

	public void close() {
		sequencer.close();
	}

	public void send(MidiMessage message, long timeStamp) {
		if (midiFileOutType == MidiFileOutType.DIRECT) {
			// Directly send the message to the out system
			// TODO choose device
		} else if(midiFileOutType == MidiFileOutType.DMX) {
			// Map the midi to DMX out
			midi2DmxConverter.processMidiEvent(message, timeStamp, midi2DmxMapping);
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
