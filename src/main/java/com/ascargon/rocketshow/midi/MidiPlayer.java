package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;
import com.ascargon.rocketshow.song.file.MidiFile.MidiFileOutType;

public class MidiPlayer implements Receiver {

	final static Logger logger = Logger.getLogger(MidiPlayer.class);

	private Sequencer sequencer;

	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;

	private Midi2DmxMapping midi2DmxMapping;

	private Midi2DmxConverter midi2DmxConverter;

	private Manager manager;
	
	private Timer connectTimer;

	public MidiPlayer(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
		this.midi2DmxConverter = manager.getMidi2DmxConverter();
	}

	public void setPositionInMillis(long position) {
		sequencer.setMicrosecondPosition(position);
	}

	private void connectMidiSender() throws MidiUnavailableException {
		MidiDevice midiDevice = manager.getSettings().getMidiInDevice();

		javax.sound.midi.MidiDevice hardwareMidiDevice = MidiUtil.getHardwareMidiDevice(manager.getSettings().getMidiOutDevice(), MidiDirection.OUT);
		
		if (hardwareMidiDevice == null) {
			logger.warn("MIDI output device not found. Try again in 2 seconds.");
			
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					try {
						// Send the universe
						connectMidiSender();
					} catch (Exception e) {
						logger.error("Could not connect to MIDI output device", e);
					}

					connectTimer.cancel();
					connectTimer = null;
				}
			};

			connectTimer = new Timer();
			connectTimer.schedule(timerTask, 2000);
			
			return;
		}
		
		// We found the device
		sequencer.getTransmitter().setReceiver(hardwareMidiDevice.getReceiver());
		
		logger.info("Successfully connected to output MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName() + "\"");
	}
	
	public void load(File file) throws Exception {
		if (sequencer != null) {
			if (sequencer.isOpen()) {
				sequencer.close();
			}
		}

		sequencer = MidiSystem.getSequencer(false);
		sequencer.open();

		InputStream is = new BufferedInputStream(new FileInputStream(file));
		sequencer.setSequence(is);
		
		if (midiFileOutType == MidiFileOutType.DIRECT) {
			// Connect the sequencer to the MIDI output device
			connectMidiSender();
		} else if(midiFileOutType == MidiFileOutType.DMX) {
			// Connect the sequencer to the this receiver for DMX mapping
			sequencer.getTransmitter().setReceiver(this);
		}
	}

	public void play() {
		logger.debug("Starting sequencer from position " + sequencer.getMicrosecondPosition());
		sequencer.start();
	}

	public void pause() {
		sequencer.stop();
	}

	public void stop() {
		sequencer.stop();
		sequencer.setMicrosecondPosition(0);
	}

	public void close() {
		sequencer.close();
	}

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
				logger.error("Could not send DMX signal from MIDI file", e);
			}
		}
	}

	public MidiFileOutType getMidiFileOutType() {
		return midiFileOutType;
	}

	public void setMidiFileOutType(MidiFileOutType midiFileOutType) {
		this.midiFileOutType = midiFileOutType;
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

}
