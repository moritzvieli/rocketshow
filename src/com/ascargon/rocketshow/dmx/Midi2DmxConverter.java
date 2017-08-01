package com.ascargon.rocketshow.dmx;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.ascargon.rocketshow.midi.Note;

public class Midi2DmxConverter {

	private DmxSignalSender dmxSignalSender;
	
	public Midi2DmxConverter (DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}
	
	public void processMidiEvent(MidiMessage message, long timeStamp, Midi2DmxMapping midi2DmxMapping) {
		// TODO Map the MIDI event and send the appropriate DMX signal
		
		if (message instanceof ShortMessage) {
			ShortMessage sm = (ShortMessage) message;
			int channel = sm.getChannel();

			if (sm.getCommand() == ShortMessage.NOTE_ON) {
				int key = sm.getData1();
				//int velocity = sm.getData2();
				Note note = new Note(key);
				System.out.println("Channel " + channel + " note on " + note);
			} else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
				int key = sm.getData1();
				//int velocity = sm.getData2();
				Note note = new Note(key);
				System.out.println("Channel " + channel + " note off " + note);
			} else {
				System.out.println("Command:" + sm.getCommand());
			}
		}
	}
	
	public DmxSignalSender getDmxSignalSender() {
		return dmxSignalSender;
	}

	public void setDmxSignalSender(DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}
	
}
