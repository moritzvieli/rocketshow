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

/**
 * Handle the current state of the device and notify all connected clients on
 * updates.
 *
 * @author Moritz A. Vieli
 */
@ServerEndpoint(value = "/state")
public class StateManager {

	final static Logger logger = Logger.getLogger(StateManager.class);
	
	private State currentState;

	private static List<Session> activeSessions = new ArrayList<Session>();

	public StateManager() {
		currentState = new State();
	}
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		activeSessions.add(session);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		activeSessions.remove(session);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.error("Got websocket error", throwable);
	}
	
	public void notifyClients() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(currentState);

		for (Session activeSession : activeSessions) {
			activeSession.getBasicRemote().sendText(serialized);
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
}
