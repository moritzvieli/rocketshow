package com.ascargon.rocketshow.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.Song.PlayState;

/**
 * Handle the current state of the device and notify all connected clients on
 * updates.
 *
 * @author Moritz A. Vieli
 */
@ServerEndpoint(value = "/state")
public class StateManager {

	final static Logger logger = Logger.getLogger(StateManager.class);

	private static List<Session> activeSessions = new ArrayList<Session>();

	private Manager manager;

	public StateManager() {
	}

	public void load(Manager manager) {
		this.manager = manager;
	}

	@OnOpen
	public void onOpen(Session session) throws Exception {
		activeSessions.add(session);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		activeSessions.remove(session);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.error("Got websocket error", throwable);

		// Session maybe closed?
		activeSessions.remove(session);
	}

	public State getCurrentState() {
		State currentState = new State();

		currentState.setPlayState(PlayState.STOPPED);
		currentState.setCurrentSongIndex(0);

		if (manager != null) {
			if (manager.getCurrentSetList() != null) {
				if (manager.getCurrentSetList().getCurrentSong() != null) {
					currentState.setPlayState(manager.getCurrentSetList().getCurrentSong().getPlayState());
				}
				currentState.setCurrentSongIndex(manager.getCurrentSetList().getCurrentSongIndex());
			}
		}

		return currentState;
	}

	private String getSerializedState() throws Exception {
		State currentState = getCurrentState();

		// Convert the object to json
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(currentState);
	}

	public void notifyClients() throws Exception {
		String state = getSerializedState();

		// Send the state to each connected client
		for (Session activeSession : activeSessions) {
			try {
				if (activeSession.isOpen()) {
					activeSession.getBasicRemote().sendText(state);
				}
			} catch (IOException e) {
				// Session maybe closed?
				activeSessions.remove(activeSession);
			}
		}
	}

}
