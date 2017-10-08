package com.ascargon.rocketshow.midi;

import java.io.IOException;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;

public class MidiReceiver implements Receiver {

	final static Logger logger = Logger.getLogger(MidiReceiver.class);

	private Manager manager;

	public MidiReceiver(Manager manager) {
		this.manager = manager;
	}

	public void load() throws MidiUnavailableException {
		MidiDevice midiDevice = manager.getSettings().getMidiInDevice();

		// Get the incoming MIDI device
		logger.info("Listening to input MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName() + "\"");

		javax.sound.midi.MidiDevice hardwareMidiDevice = MidiUtil.getHardwareMidiDevice(midiDevice, MidiDirection.IN);

		if (hardwareMidiDevice == null) {
			logger.warn("Hardware MIDI device not found");
			return;
		}

		hardwareMidiDevice.open();
		hardwareMidiDevice.getTransmitter().setReceiver(this);
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
		int velocity = shortMessage.getData2();

		String loggingCommand = "";
		
		if(command == ShortMessage.NOTE_ON) {
			loggingCommand = "ON";
		} else if (command == ShortMessage.NOTE_OFF) {
			loggingCommand = "OFF";
		} else {
			loggingCommand = "MISC";
		}
		
		logger.debug("Note " + loggingCommand + ", channel = " + channel + ", note = " + note + ", velocity = " + velocity);
		
		if (manager.getSettings().isLiveDmx()) {
			// Route incoming MIDI events through the global MIDI to DMX mapping
			try {
				manager.getMidi2DmxConverter().processMidiEvent(command, channel, note, velocity, timeStamp,
						manager.getSettings().getLiveMidi2DmxMapping());
			} catch (IOException e) {
				logger.error("Could not send DMX signal from live MIDI", e);
			}
		}

		// Process MIDI events as actions according to the settings
		try {
			manager.getMidi2ActionConverter().processMidiEvent(command, channel, note, timeStamp,
					manager.getSettings().getLiveMidi2ActionMapping());
		} catch (Exception e) {
			logger.error("Could not execute action from live MIDI", e);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
