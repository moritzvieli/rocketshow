package com.ascargon.rocketshow.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

/**
 * Receive MIDI messages and send them to remote devices.
 *
 * @author Moritz A. Vieli
 */
public class Midi2RemoteReceiver implements Receiver {

	final static Logger logger = Logger.getLogger(Midi2RemoteReceiver.class);

	public Midi2RemoteReceiver(Manager manager) throws MidiUnavailableException {
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;

			int command = shortMessage.getCommand();
			int channel = shortMessage.getChannel();
			int note = shortMessage.getData1();
			int velocity = shortMessage.getData2();

			// TODO
		}
	}

	@Override
	public void close() {
		// Nothing to do at the moment
	}

}
