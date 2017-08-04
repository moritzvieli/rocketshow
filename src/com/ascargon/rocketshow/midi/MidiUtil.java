package com.ascargon.rocketshow.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class MidiUtil {

	public static MidiDevice getHardwareMidiDevice(com.ascargon.rocketshow.midi.MidiDevice midiDevice)
			throws MidiUnavailableException {
		// Get a hardware device for a given MIDI device
		MidiDevice.Info[] midiDeviceInfos = MidiSystem.getMidiDeviceInfo();

		// Search for a device with same id and name
		if (midiDeviceInfos.length > midiDevice.getId()) {
			if (midiDeviceInfos[midiDevice.getId()].getName().equals(midiDevice.getName())) {
				return MidiSystem.getMidiDevice(midiDeviceInfos[midiDevice.getId()]);
			}
		}

		// Search for a device with the same name
		for (int i = 0; i < midiDeviceInfos.length; i++) {
			if (midiDeviceInfos[i].getName().equals(midiDevice.getName())) {
				return MidiSystem.getMidiDevice(midiDeviceInfos[i]);
			}
		}

		// Search for a device with the same id
		if (midiDeviceInfos.length > midiDevice.getId()) {
			return MidiSystem.getMidiDevice(midiDeviceInfos[midiDevice.getId()]);
		}

		return null;
	}

	private static List<com.ascargon.rocketshow.midi.MidiDevice> getMidiDevices(boolean in, boolean out) throws MidiUnavailableException {
		// Get all available MIDI devices
		MidiDevice.Info[] midiDeviceInfos = MidiSystem.getMidiDeviceInfo();
		List<com.ascargon.rocketshow.midi.MidiDevice> midiDeviceList = new ArrayList<com.ascargon.rocketshow.midi.MidiDevice>();

		for (int i = 0; i < midiDeviceInfos.length; i++) {
			MidiDevice hardwareMidiDevice = MidiSystem.getMidiDevice(midiDeviceInfos[i]);
			if ((in && hardwareMidiDevice.getMaxReceivers() != 0)
					|| (out && hardwareMidiDevice.getMaxTransmitters() != 0)) {
				com.ascargon.rocketshow.midi.MidiDevice midiDevice = new com.ascargon.rocketshow.midi.MidiDevice();
				midiDevice.setId(i);
				midiDevice.setName(midiDeviceInfos[i].getName());
				midiDeviceList.add(midiDevice);
			}
		}

		return midiDeviceList;
	}

	public static List<com.ascargon.rocketshow.midi.MidiDevice> getInMidiDevices() throws MidiUnavailableException {
		return getMidiDevices(true, false);
	}

	public static List<com.ascargon.rocketshow.midi.MidiDevice> getOutMidiDevices() throws MidiUnavailableException {
		return getMidiDevices(false, true);
	}

}
