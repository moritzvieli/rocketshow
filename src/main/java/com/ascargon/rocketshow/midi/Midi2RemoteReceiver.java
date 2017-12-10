package com.ascargon.rocketshow.midi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.RemoteDevice;

/**
 * Receive MIDI messages and send them to remote devices.
 *
 * @author Moritz A. Vieli
 */
public class Midi2RemoteReceiver implements Receiver {

	final static Logger logger = Logger.getLogger(Midi2RemoteReceiver.class);

	private MidiMapping midiMapping;

	private List<Integer> remoteDeviceIdList = new ArrayList<Integer>();

	private Manager manager;

	public Midi2RemoteReceiver(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (!(message instanceof ShortMessage)) {
			return;
		}

		MidiSignal midiSignal = new MidiSignal((ShortMessage) message);
		
		try {
			MidiMapper.processMidiEvent(midiSignal, midiMapping);
		} catch (IOException e) {
			logger.error("Could not map MIDI signal for remote receiver", e);
		}

		String apiUrl = "midi/send-message?command=" + midiSignal.getCommand() + "&channel=" + midiSignal.getChannel()
				+ "&note=" + midiSignal.getNote() + "&velocity" + midiSignal.getVelocity();

		for (Integer remoteDeviceId : remoteDeviceIdList) {
			RemoteDevice remoteDevice = manager.getSettings().getRemoteDeviceById(remoteDeviceId);

			try {
				remoteDevice.doPost(apiUrl);
			} catch (Exception e) {
				logger.error("Could not send MIDI message to remote device '" + remoteDevice.getHost() + "' ("
						+ remoteDeviceId + ")", e);
			}
		}
	}

	@Override
	public void close() {
		// Nothing to do at the moment
	}

	public List<Integer> getRemoteDeviceIdList() {
		return remoteDeviceIdList;
	}

	public void setRemoteDeviceIdList(List<Integer> remoteDeviceIdList) {
		this.remoteDeviceIdList = remoteDeviceIdList;
	}

	public MidiMapping getMidiMapping() {
		return midiMapping;
	}

	public void setMidiMapping(MidiMapping midiMapping) {
		this.midiMapping = midiMapping;
	}

}
