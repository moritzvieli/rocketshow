package com.ascargon.rocketshow.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.dmx.Midi2DmxReceiver;

/**
 * Defines, where to route the output of MIDI signals.
 *
 * @author Moritz A. Vieli
 */
@XmlRootElement
public class MidiRouting implements MidiDeviceConnectedListener {

	final static Logger logger = Logger.getLogger(MidiRouting.class);

	public enum MidiDestination {
		OUT_DEVICE, DMX, REMOTE, ACTIONS
	}

	private MidiDestination midiDestination = MidiDestination.OUT_DEVICE;

	private Manager manager;

	private javax.sound.midi.MidiDevice midiOutDevice;

	private Midi2DmxReceiver midi2DmxReceiver;
	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private Midi2RemoteReceiver midi2RemoteReceiver;

	private Transmitter transmitter;
	private Receiver receiver;

	public MidiRouting() {
	}

	public void load(Manager manager) throws MidiUnavailableException {
		this.manager = manager;

		manager.addMidiOutDeviceConnectedListener(this);

		midi2DmxReceiver = new Midi2DmxReceiver(manager);
		midi2DmxReceiver.setMidi2DmxMapping(midi2DmxMapping);

		midi2RemoteReceiver = new Midi2RemoteReceiver(manager);

		refreshTransmitterConnection();
	}

	public void close() {
		if (midi2DmxReceiver != null) {
			midi2DmxReceiver.close();
		}

		manager.removeMidiOutDeviceConnectedListener(this);
	}

	private void refreshTransmitterConnection() {
		if (midiDestination == MidiDestination.OUT_DEVICE) {
			// Connect the transmitter to the out device
			if (midiOutDevice == null) {
				return;
			}

			if (midiOutDevice != null) {
				try {
					receiver = midiOutDevice.getReceiver();
				} catch (MidiUnavailableException e) {
					logger.error("Could not connect transmitter to receiver", e);
				}
			}
		} else if (midiDestination == MidiDestination.DMX) {
			// Connect the transmitter to the DMX receiver
			receiver = midi2DmxReceiver;
		} else if (midiDestination == MidiDestination.REMOTE) {
			// Connect the transmitter to the remote receiver
			receiver = midi2RemoteReceiver;
		}

		if (transmitter == null) {
			return;
		}

		if (receiver == null) {
			return;
		}

		transmitter.setReceiver(receiver);
	}

	public void sendMidiMessage(int command, int channel, int note, int velocity) {
		if (receiver == null) {
			return;
		}

		ShortMessage midiMsg = new ShortMessage();
		long timeStamp = -1;

		try {
			// (ShortMessage.NOTE_ON, 60, 93);
			midiMsg.setMessage(command, channel, note, velocity);
		} catch (InvalidMidiDataException e) {
			logger.error("Could not send MIDI message", e);
		}

		receiver.send(midiMsg, timeStamp);
	}

	@Override
	public void deviceConnected(javax.sound.midi.MidiDevice midiDevice) {
		this.midiOutDevice = midiDevice;
		refreshTransmitterConnection();
	}

	@Override
	public void deviceDisconnected(javax.sound.midi.MidiDevice midiDevice) {
		this.midiOutDevice = null;
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

	@XmlTransient
	public Midi2DmxReceiver getMidi2DmxReceiver() {
		return midi2DmxReceiver;
	}

	public void setMidi2DmxReceiver(Midi2DmxReceiver midi2DmxReceiver) {
		this.midi2DmxReceiver = midi2DmxReceiver;
	}

	@XmlTransient
	public Transmitter getTransmitter() {
		return transmitter;
	}

	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
		refreshTransmitterConnection();
	}

	public MidiDestination getMidiDestination() {
		return midiDestination;
	}

	public void setMidiDestination(MidiDestination midiDestination) {
		this.midiDestination = midiDestination;
		refreshTransmitterConnection();
	}

}
