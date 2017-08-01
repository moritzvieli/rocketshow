package com.ascargon.rocketshow.song.file;

import java.io.File;

import javax.sound.midi.MidiUnavailableException;
import javax.xml.bind.annotation.XmlElement;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiPlayer;

public class MidiFile extends com.ascargon.rocketshow.song.file.File {

	public enum MidiFileOutType {
		DIRECT, DMX
	}

	private MidiFileOutType midiFileOutType = MidiFileOutType.DIRECT;

	private Midi2DmxMapping midi2DmxMapping;

	private MidiPlayer midiPlayer;

	public void load() {
		try {
			midiPlayer = new MidiPlayer(this.getManager());
			midiPlayer.load(new File(this.getPath()));
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		midiPlayer.close();
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
