package com.ascargon.rocketshow.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import com.ascargon.rocketshow.Manager;

public class MidiReceiver implements Receiver {

	private Manager manager;

	public MidiReceiver(Manager manager) {
		this.manager = manager;
	}

	public void load() throws MidiUnavailableException {
		// Get the incoming MIDI device
		javax.sound.midi.MidiDevice midiDevice = MidiUtil.getHardwareMidiDevice(manager.getSettings().getMidiInDevice());
		
		if(midiDevice == null) {
			return;
		}
		
		midiDevice.getTransmitter().setReceiver(this);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (manager.getSettings().isLiveDmx()) {
			// Route incoming MIDI events through the global MIDI to DMX mapping
			manager.getMidi2DmxConverter().processMidiEvent(message, timeStamp, manager.getSettings().getLiveMidi2DmxMapping());
		}

		// TODO Process MIDI events as actions according to the settings (midiActionMapping)
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
