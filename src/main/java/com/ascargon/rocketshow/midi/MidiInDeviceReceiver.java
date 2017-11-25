package com.ascargon.rocketshow.midi;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;

/**
 * Handle the MIDI events from the currently connected MIDI input device.
 *
 * @author Moritz A. Vieli
 */
public class MidiInDeviceReceiver implements Receiver {

	final static Logger logger = Logger.getLogger(MidiInDeviceReceiver.class);

	private Manager manager;

	private Timer connectTimer;

	private javax.sound.midi.MidiDevice midiReceiver;

	private List<MidiRouting> midiRoutingList;

	public MidiInDeviceReceiver(Manager manager) {
		this.manager = manager;

		midiRoutingList = manager.getSettings().getDeviceInMidiRoutingList();
	}

	/**
	 * Connect the MIDI player to a sender, if required. Also call this method,
	 * if you change the settings or want to reconnect the device.
	 * 
	 * @throws MidiUnavailableException
	 */
	public void connectMidiReceiver() throws MidiUnavailableException {
		if (midiReceiver != null && midiReceiver.isOpen()) {
			// We already have an open receiver -> close this one
			try {
				midiReceiver.close();
			} catch (Exception e) {
			}
		}

		MidiDevice midiDevice = manager.getSettings().getMidiInDevice();

		logger.debug("Try connecting to input MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName() + "\"");

		midiReceiver = MidiUtil.getHardwareMidiDevice(midiDevice, MidiDirection.IN);

		if (midiReceiver == null) {
			logger.debug("MIDI input device not found. Try again in 5 seconds.");

			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					try {
						connectMidiReceiver();
					} catch (Exception e) {
						logger.debug("Could not connect to MIDI input device", e);
					}
				}
			};

			connectTimer = new Timer();
			connectTimer.schedule(timerTask, 5000);

			return;
		}

		// We found the device
		if (connectTimer != null) {
			connectTimer.cancel();
			connectTimer = null;
		}

		midiReceiver.open();

		// Set the MIDI routing receivers
		for(MidiRouting midiRouting : midiRoutingList) {
			midiRouting.setTransmitter(midiReceiver.getTransmitter());
		}

		// Also set this class as a second receiver to execute the MIDI actions
		// (midiReceiver.getTransmitter returns a different transmitter each
		// time)
		midiReceiver.getTransmitter().setReceiver(this);

		logger.info("Successfully connected to input MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName()
				+ "\"");
	}

	public void load() throws MidiUnavailableException {
		MidiDevice midiDevice = manager.getSettings().getMidiInDevice();

		// Get the incoming MIDI device
		logger.info(
				"Setting up listener to input MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName() + "\"");

		connectMidiReceiver();
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (!(message instanceof ShortMessage)) {
			return;
		}

		ShortMessage shortMessage = (ShortMessage) message;

		int command = shortMessage.getCommand();
		int channel = shortMessage.getChannel();
		int note = shortMessage.getData1();

		// int velocity = shortMessage.getData2();
		//
		// String loggingCommand = "";
		//
		// if (command == ShortMessage.NOTE_ON) {
		// loggingCommand = "ON";
		// } else if (command == ShortMessage.NOTE_OFF) {
		// loggingCommand = "OFF";
		// } else {
		// loggingCommand = "MISC";
		// }

		// logger.debug(
		// "Note " + loggingCommand + ", channel = " + channel + ", note = " +
		// note + ", velocity = " + velocity);

		// Process MIDI events as actions according to the settings
		try {
			manager.getMidi2ActionConverter().processMidiEvent(command, channel, note, timeStamp,
					manager.getSettings().getMidi2ActionMapping());
		} catch (Exception e) {
			logger.error("Could not execute action from live MIDI", e);
		}
	}

	@Override
	public void close() {
		if(connectTimer != null) {
			connectTimer.cancel();
		}
		
		if (midiReceiver != null && midiReceiver.isOpen()) {
			midiReceiver.close();
		}
	}

}
