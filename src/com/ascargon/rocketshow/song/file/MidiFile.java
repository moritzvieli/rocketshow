package com.ascargon.rocketshow.song.file;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiPlayer;

public class MidiFile extends com.ascargon.rocketshow.song.file.File {

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
		midiPlayer.setMidi2DmxMapping(midi2DmxMapping);

		if (this.getOffsetInMillis() >= 0) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					midiPlayer.play();
				}
			}, this.getOffsetInMillis());
		} else {
			midiPlayer.setPositionInMillis(this.getOffsetInMillis() * -1);
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
