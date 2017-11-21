package com.ascargon.rocketshow.midi;

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

	private List<Integer> remoteDeviceIdList = new ArrayList<Integer>();

	private Manager manager;

	public Midi2RemoteReceiver(Manager manager) throws MidiUnavailableException {
		this.manager = manager;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;

			int command = shortMessage.getCommand();
			int channel = shortMessage.getChannel();
			int note = shortMessage.getData1();
			int velocity = shortMessage.getData2();

			String apiUrl = "midi/send-message?command=" + command + "&channel=" + channel + "&note=" + note
					+ "&velocity" + velocity;

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

}
