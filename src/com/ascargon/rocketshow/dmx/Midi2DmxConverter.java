package com.ascargon.rocketshow.dmx;

import java.io.IOException;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping.MappingType;

public class Midi2DmxConverter {

	final static Logger logger = Logger.getLogger(Midi2DmxConverter.class);

	private DmxSignalSender dmxSignalSender;

	public Midi2DmxConverter(DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}

	private int getChannelTo(int channelFrom, Midi2DmxMapping midi2DmxMapping) {
		// Search the channel map on the current mapping
		if (midi2DmxMapping.getChannelMap() != null) {
			for (int i = 0; i < midi2DmxMapping.getChannelMap().size(); i++) {
				if (midi2DmxMapping.getChannelMap().get(i).getChannelFrom() == channelFrom) {
					return midi2DmxMapping.getChannelMap().get(i).getChannelTo();
				}
			}
		}

		// We haven't found a mapping -> search the parent
		if (midi2DmxMapping.getParent() != null) {
			if (!midi2DmxMapping.getParent().isOverrideParent()) {
				return getChannelTo(channelFrom, midi2DmxMapping.getParent());
			}
		}

		// There is no mapping on the whole parent-chain -> channelTo =
		// channelFrom
		return channelFrom;
	}

	private int getChannelOffset(Midi2DmxMapping midi2DmxMapping) {
		// Check the current mapping
		if (midi2DmxMapping.getChannelOffset() != null) {
			return midi2DmxMapping.getChannelOffset();
		}

		// There is no offset on the current mapping -> check the parent
		if (midi2DmxMapping.getParent() != null) {
			if (!midi2DmxMapping.getParent().isOverrideParent()) {
				return getChannelOffset(midi2DmxMapping.getParent());
			}
		}

		// There is no offset on the whole parent-chain -> return default 0
		return 0;
	}

	private int mapChannel(int channelFrom, Midi2DmxMapping midi2DmxMapping) {
		return getChannelTo(channelFrom, midi2DmxMapping) + getChannelOffset(midi2DmxMapping);
	}

	private void mapSimple(int command, int channel, int note, int velocity, Midi2DmxMapping midi2DmxMapping)
			throws IOException {

		if (command == ShortMessage.NOTE_ON) {
			int channelTo = mapChannel(note, midi2DmxMapping);
			int valueTo = velocity * 2;

			dmxSignalSender.send(channelTo, valueTo);
		} else if (command == ShortMessage.NOTE_OFF) {
			int channelTo = mapChannel(note, midi2DmxMapping);
			int valueTo = 0;

			dmxSignalSender.send(channelTo, valueTo);
		}
	}

	private void mapExact(int command, int channel, int note, int velocity, Midi2DmxMapping midi2DmxMapping)
			throws IOException {

		// TODO
	}

	public void processMidiEvent(MidiMessage message, long timeStamp, Midi2DmxMapping midi2DmxMapping)
			throws IOException {

		// Map the MIDI event and send the appropriate DMX signal
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;

			int command = shortMessage.getCommand();

			// Only react to NOTE_ON/NOTE_OFF events
			if (command != ShortMessage.NOTE_ON && command != ShortMessage.NOTE_OFF) {
				return;
			}

			int channel = shortMessage.getChannel();
			int note = shortMessage.getData1();
			int velocity = shortMessage.getData2();

			String loggingCommand = "";
			
			if(command == ShortMessage.NOTE_ON) {
				loggingCommand = "ON";
			} else if (command == ShortMessage.NOTE_OFF) {
				loggingCommand = "OFF";
			}
			
			logger.debug("Note " + loggingCommand + ", channel = " + channel + ", note = " + note + ", velocity = " + velocity);
			
			if (midi2DmxMapping.getMappingType() == MappingType.SIMPLE) {
				mapSimple(command, channel, note, velocity, midi2DmxMapping);
			} else if (midi2DmxMapping.getMappingType() == MappingType.EXACT) {
				mapExact(command, channel, note, velocity, midi2DmxMapping);
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
