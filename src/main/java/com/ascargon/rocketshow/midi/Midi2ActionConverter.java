package com.ascargon.rocketshow.midi;

import java.io.IOException;

import javax.sound.midi.ShortMessage;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.RemoteDevice;
import com.ascargon.rocketshow.midi.ActionMapping.MidiAction;

public class Midi2ActionConverter {

	final static Logger logger = Logger.getLogger(Midi2ActionConverter.class);

	private Manager manager;

	public Midi2ActionConverter(Manager manager) {
		this.manager = manager;
	}

	/**
	 * Does this action mapping match to the current MIDI message and should the
	 * action be executed?
	 * 
	 * @param actionMapping
	 * @param channel
	 * @param note
	 * @return
	 */
	private boolean isActionMappingMatch(ActionMapping actionMapping, int channel, int note) {
		if ((actionMapping.getChannelFrom() == null || actionMapping.getChannelFrom() == channel)
				&& (actionMapping.getNoteFrom() == null || actionMapping.getNoteFrom() == note)) {
			return true;
		}

		return false;
	}

	private void executeActionOnRemoteDevice(MidiAction action, RemoteDevice remoteDevice)
			throws ClientProtocolException, IOException {

		String apiUrl = "";

		switch (action) {
		case PLAY:
			apiUrl = "transport/play";
			break;
		case PAUSE:
			apiUrl = "transport/pause";
			break;
		case TOGGLE_PLAY:
			apiUrl = "transport/toggle-play";
			break;
		case RESUME:
			apiUrl = "transport/resume";
			break;
		case STOP:
			apiUrl = "transport/stop";
			break;
		case NEXT_SONG:
			apiUrl = "transport/next-song";
			break;
		case PREVIOUS_SONG:
			apiUrl = "transport/previous-song";
			break;
		case SET_SONG_INDEX:
			apiUrl = "transport/set-song-index?index=" + manager.getCurrentSetList().getCurrentSongIndex();
			break;
		default:
			logger.warn("Action '" + action.toString() + "' is unknown for remote devices and cannot be executed");
			return;
		}

		// Execute the post request with the given url
		remoteDevice.doPost(apiUrl);
	}

	private void executeActionLocally(MidiAction action) throws Exception {
		// Execute the action locally
		switch (action) {
		case PLAY:
			manager.getCurrentSetList().play();
			break;
		case PAUSE:
			manager.getCurrentSetList().pause();
			break;
		case TOGGLE_PLAY:
			manager.getCurrentSetList().togglePlay();
			break;
		case RESUME:
			manager.getCurrentSetList().resume();
			break;
		case STOP:
			manager.getCurrentSetList().stop();
			break;
		case NEXT_SONG:
			manager.getCurrentSetList().nextSong();
			break;
		case PREVIOUS_SONG:
			manager.getCurrentSetList().previousSong();
			break;
		default:
			logger.warn("Action '" + action.toString() + "' is locally unknown and cannot be executed");
			break;
		}
	}

	/**
	 * Execute the action according to the actionMapping-element.
	 * 
	 * @param actionMapping
	 * @throws Exception
	 */
	private void executeActionMappingAction(ActionMapping actionMapping) throws Exception {
		MidiAction action = actionMapping.getAction();

		if (actionMapping.isExecuteLocally()) {
			executeActionLocally(action);
		}

		// Execute the action on each specified remote device
		for (int id : actionMapping.getRemoteDeviceIds()) {
			RemoteDevice remoteDevice = manager.getSettings().getRemoteDeviceById(id);

			if (remoteDevice == null) {
				logger.warn("No remoteDevice could be found in the settings with id " + id);
			} else {
				executeActionOnRemoteDevice(action, remoteDevice);
			}
		}
	}

	public void processMidiEvent(int command, int channel, int note, long timeStamp,
			Midi2ActionMapping midi2ActionMapping) throws Exception {

		// Map the MIDI event and execute the appropriate actions

		// Only react to NOTE_ON events
		if (command != ShortMessage.NOTE_ON) {
			return;
		}

		// Search for and execute all required actions
		for (ActionMapping actionMapping : midi2ActionMapping.getActionMappingList()) {
			if (isActionMappingMatch(actionMapping, channel, note)) {
				executeActionMappingAction(actionMapping);
			}
		}
	}

}
