package com.ascargon.rocketshow.dmx;

import java.io.IOException;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

/**
 * Receive MIDI messages and map them to DMX signals.
 *
 * @author Moritz A. Vieli
 */
public class Midi2DmxReceiver implements Receiver {

	final static Logger logger = Logger.getLogger(Midi2DmxReceiver.class);

	private Midi2DmxMapping midi2DmxMapping;

	private Midi2DmxConverter midi2DmxConverter;

	public Midi2DmxReceiver(Manager manager) throws MidiUnavailableException {
		this.midi2DmxConverter = manager.getMidi2DmxConverter();
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// Map the midi to DMX out
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;

			int command = shortMessage.getCommand();
			int channel = shortMessage.getChannel();
			int note = shortMessage.getData1();
			int velocity = shortMessage.getData2();

			try {
				midi2DmxConverter.processMidiEvent(command, channel, note, velocity, timeStamp, midi2DmxMapping);
			} catch (IOException e) {
				logger.error("Could not send DMX signal", e);
			}
		}
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

	@Override
	public void close() {
		// Nothing to do at the moment
	}

}
