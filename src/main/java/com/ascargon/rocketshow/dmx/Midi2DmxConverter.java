package com.ascargon.rocketshow.dmx;

import java.io.IOException;

import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping.MappingType;
import com.ascargon.rocketshow.midi.MidiSignal;

public class Midi2DmxConverter {

	final static Logger logger = Logger.getLogger(Midi2DmxConverter.class);

	private DmxSignalSender dmxSignalSender;

	public Midi2DmxConverter(DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}

	private void mapSimple(MidiSignal midiSignal) throws IOException {
		if (midiSignal.getCommand() == ShortMessage.NOTE_ON) {
			int valueTo = midiSignal.getVelocity() * 2;
			
			// Extend the last note to the max
			// TODO enable this feature by a mapping-setting
			if(valueTo == 254) {
				valueTo = 255;
			}

			dmxSignalSender.send(midiSignal.getNote(), valueTo);
		} else if (midiSignal.getCommand() == ShortMessage.NOTE_OFF) {
			int valueTo = 0;
			dmxSignalSender.send(midiSignal.getNote(), valueTo);
		}
	}

	private void mapExact(MidiSignal midiSignal, Midi2DmxMapping midi2DmxMapping)
			throws IOException {

		// TODO
	}

	public void processMidiEvent(MidiSignal midiSignal, Midi2DmxMapping midi2DmxMapping) throws IOException {
		// Map the MIDI event and send the appropriate DMX signal

		// Only react to NOTE_ON/NOTE_OFF events
		if (midiSignal.getCommand() != ShortMessage.NOTE_ON && midiSignal.getCommand() != ShortMessage.NOTE_OFF) {
			return;
		}

		if (midi2DmxMapping.getMappingType() == MappingType.SIMPLE) {
			mapSimple(midiSignal);
		} else if (midi2DmxMapping.getMappingType() == MappingType.EXACT) {
			mapExact(midiSignal, midi2DmxMapping);
		}
	}

	public DmxSignalSender getDmxSignalSender() {
		return dmxSignalSender;
	}

	public void setDmxSignalSender(DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}

}
