package com.ascargon.rocketshow;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiDevice;
import com.ascargon.rocketshow.midi.MidiUtil;

@XmlRootElement
public class Settings {

	private String defaultImagePath;
	
	private boolean liveDmx;
	
	private Midi2DmxMapping fileMidi2DmxMapping;
	private Midi2DmxMapping liveMidi2DmxMapping;
	
	private MidiDevice midiInDevice;
	private MidiDevice midiOutDevice;
	
	public Settings () {
		// Initialize default settings
		defaultImagePath = null;
		
		liveDmx = false;
		
		// Default channel mapping
		HashMap<Integer, Integer> channelMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < 128; i++) {
			channelMap.put(i, i);	
		}
		
		// File MIDI to DMX mapping
		fileMidi2DmxMapping = new Midi2DmxMapping();
		fileMidi2DmxMapping.setChannelOffset(0);
		fileMidi2DmxMapping.setChannelMap(channelMap);
		
		// Live MIDI to DMX mapping
		liveMidi2DmxMapping = new Midi2DmxMapping();
		liveMidi2DmxMapping.setChannelOffset(0);
		liveMidi2DmxMapping.setChannelMap(channelMap);
		
		List<MidiDevice> midiInDeviceList = MidiUtil.getInMidiDevices();
		if(midiInDeviceList.size() > 0) {
			midiInDevice = midiInDeviceList.get(0);
		}
		
		List<MidiDevice> midiOutDeviceList = MidiUtil.getOutMidiDevices();
		if(midiOutDeviceList.size() > 0) {
			midiOutDevice = midiOutDeviceList.get(0);
		}
	}

	@XmlElement
	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}

	@XmlElement
	public boolean isLiveDmx() {
		return liveDmx;
	}

	public void setLiveDmx(boolean liveDmx) {
		this.liveDmx = liveDmx;
	}

	@XmlElement
	public Midi2DmxMapping getFileMidi2DmxMapping() {
		return fileMidi2DmxMapping;
	}

	public void setFileMidi2DmxMapping(Midi2DmxMapping fileMidi2DmxMapping) {
		this.fileMidi2DmxMapping = fileMidi2DmxMapping;
	}

	@XmlElement
	public Midi2DmxMapping getLiveMidi2DmxMapping() {
		return liveMidi2DmxMapping;
	}

	public void setLiveMidi2DmxMapping(Midi2DmxMapping liveMidi2DmxMapping) {
		this.liveMidi2DmxMapping = liveMidi2DmxMapping;
	}

	@XmlElement
	public MidiDevice getMidiInDevice() {
		return midiInDevice;
	}

	public void setMidiInDevice(MidiDevice midiInDevice) {
		this.midiInDevice = midiInDevice;
	}

	@XmlElement
	public MidiDevice getMidiOutDevice() {
		return midiOutDevice;
	}

	public void setMidiOutDevice(MidiDevice midiOutDevice) {
		this.midiOutDevice = midiOutDevice;
	}
	
}
