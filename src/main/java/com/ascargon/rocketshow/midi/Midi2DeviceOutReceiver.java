package com.ascargon.rocketshow.midi;

import java.io.IOException;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

/**
 * Receive MIDI messages and send them to the out device.
 *
 * @author Moritz A. Vieli
 */
public class Midi2DeviceOutReceiver implements Receiver, MidiDeviceConnectedListener {

	final static Logger logger = Logger.getLogger(Midi2DeviceOutReceiver.class);

	private MidiMapping midiMapping;

	private Manager manager;

	private javax.sound.midi.MidiDevice midiOutDevice;
	
	public Midi2DeviceOutReceiver(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
		manager.addMidiOutDeviceConnectedListener(this);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (midiOutDevice == null) {
			return;
		}
		
		if (!(message instanceof ShortMessage)) {
			return;
		}

		MidiSignal midiSignal = new MidiSignal((ShortMessage) message);
		
		try {
			MidiMapper.processMidiEvent(midiSignal, midiMapping);
		} catch (IOException e) {
			logger.error("Could not map MIDI signal for remote receiver", e);
		}

		try {
			midiOutDevice.getReceiver().send(midiSignal.getShortMessage(), -1);
		} catch (Exception e) {
			logger.error("Could not send MIDI signal to out device receiver", e);
		}
	}

	@Override
	public void close() {
		if (manager != null) {
			manager.removeMidiOutDeviceConnectedListener(this);
		}
	}

	@Override
	public void deviceConnected(javax.sound.midi.MidiDevice midiDevice) {
		this.midiOutDevice = midiDevice;
	}

	@Override
	public void deviceDisconnected(javax.sound.midi.MidiDevice midiDevice) {
		this.midiOutDevice = null;
	}
	
	public MidiMapping getMidiMapping() {
		return midiMapping;
	}

	public void setMidiMapping(MidiMapping midiMapping) {
		this.midiMapping = midiMapping;
	}

}
