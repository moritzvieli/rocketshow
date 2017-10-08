package com.ascargon.rocketshow.midi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sound.midi.ShortMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.RemoteDevice;
import com.ascargon.rocketshow.midi.ActionMapping.MidiAction;

public class Midi2ActionConverter {

	final static Logger logger = Logger.getLogger(Midi2ActionConverter.class);

	private Manager manager;

	private HttpClient httpClient;

	public Midi2ActionConverter(Manager manager) {
		this.manager = manager;

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
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

	private void doPostOnRemoteDevice(String url) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = httpClient.execute(httpPost);

		// Read the response. The POST connection will not be released otherwise
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String line = "";

		while ((line = rd.readLine()) != null) {
			logger.debug("Response from remote device POST: " + line);
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			logger.error("Could not execute action on remote device with url '" + url + "'. Reason: '"
					+ response.getStatusLine().getReasonPhrase() + "'. Body: " + EntityUtils.toString(response.getEntity()));
		}
	}

	private void executeActionOnRemoteDevice(MidiAction action, RemoteDevice remoteDevice)
			throws ClientProtocolException, IOException {
		String url = "";

		// Build the url for the post request
		url += "http://";

		// Add the host
		url += remoteDevice.getHost();

		// Add the api-url
		url += "/api/";

		switch (action) {
		case PLAY:
			url += "transport/play";
			break;
		case PAUSE:
			url += "transport/pause";
			break;
		case TOGGLE_PLAY:
			url += "transport/toggle-play";
			break;
		case RESUME:
			url += "transport/resume";
			break;
		case STOP:
			url += "transport/stop";
			break;
		case NEXT_SONG:
			url += "transport/next-song";
			break;
		case PREVIOUS_SONG:
			url += "transport/previous-song";
			break;
		default:
			logger.warn("Action '" + action.toString() + "' is unknown for remote devices and cannot be executed");
			return;
		}

		// Execute the post request with the given url
		doPostOnRemoteDevice(url);
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
