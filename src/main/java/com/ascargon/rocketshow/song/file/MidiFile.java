package com.ascargon.rocketshow.song.file;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiPlayer;

public class MidiFile extends com.ascargon.rocketshow.song.file.File {

	final static Logger logger = Logger.getLogger(MidiFile.class);
	
	public enum MidiFileOutType {
		DIRECT, DMX
	}

	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private MidiPlayer midiPlayer;

	public void load() throws Exception {
		midiPlayer = new MidiPlayer(this.getManager());
		midiPlayer.load(new File(this.getPath()));
	}

	@Override
	public void play() {
		midiPlayer.setMidiFileOutType(midiFileOutType);
		midiPlayer.getMidi2DmxReceiver().setMidi2DmxMapping(midi2DmxMapping);

		if (this.getOffsetInMillis() >= 0) {
			logger.debug("Wait " + this.getOffsetInMillis() + " milliseconds before starting the midi file");
			
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					midiPlayer.play();
				}
			}, this.getOffsetInMillis());
		} else {
			logger.debug("Set MIDI file offset to " + (this.getOffsetInMillis() * -1000) + " milliseconds");
			
			midiPlayer.setPositionInMillis(this.getOffsetInMillis() * -1000);
			midiPlayer.play();
		}
	}

	@Override
	public void pause() {
		midiPlayer.pause();
	}

	@Override
	public void resume() {
		midiPlayer.play();
	}

	@Override
	public void stop() throws Exception {
		midiPlayer.stop();
	}
	
	@Override
	public void close() {
		midiPlayer.close();
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

	@XmlElement
	public MidiFileOutType getMidiFileOutType() {
		return midiFileOutType;
	}

	public void setMidiFileOutType(MidiFileOutType midiFileOutType) {
		this.midiFileOutType = midiFileOutType;
	}

}
