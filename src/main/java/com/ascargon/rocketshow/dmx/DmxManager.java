package com.ascargon.rocketshow.dmx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

import ola.OlaClient;
import ola.proto.Ola.DeviceInfoReply;
import ola.proto.Ola.UniverseInfoReply;

public class DmxManager {

	final static Logger logger = Logger.getLogger(DmxManager.class);

	private Manager manager;

	// Cache the channel values and send them each time
	private HashMap<Integer, Integer> channelValues;

	private OlaClient olaClient;

	// Delay sending of the universe because of 2 reasons:
	// - Performance: Sending the whole universe each midi event is not fast
	// enough
	// - Glitches: If we send each event separately, you can see the transitions
	// even if they're not meant to be (e.g. activate two channels at the same
	// time, but sentseparately)
	private Timer timer;

	public DmxManager(Manager manager) {
		this.manager = manager;

		try {
			olaClient = new OlaClient();
		} catch (Exception e) {
			logger.error("Could not initialize OLA DMX client", e);
		}

		reset();
	}

	public void reset() {
		// Initialize the universe
		channelValues = new HashMap<Integer, Integer>();

		for (int i = 0; i < 512; i++) {
			channelValues.put(i, 0);
		}

		try {
			sendUniverse();
		} catch (IOException e) {
			logger.error("Could not initialize the DMX universe", e);
		}
	}

	private void sendUniverse() throws IOException {
		if (olaClient == null) {
			return;
		}

		short[] universe = new short[512];

		for (int i = 0; i < channelValues.size(); i++) {
			universe[i] = (short) channelValues.get(i).intValue();
		}

		olaClient.streamDmx(1, universe);
	}

	public void send(int channel, int value) throws IOException {
		logger.trace("Setting DMX channel " + channel + " to value " + value);

		channelValues.put(channel, value);

		// Schedule the specified count of executions in the specified delay
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				try {
					// Send the universe
					sendUniverse();
				} catch (IOException e) {
					logger.error("Could not send the DMX universe", e);
				}

				if (timer != null) {
					timer.cancel();
				}

				timer = null;
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, manager.getSettings().getDmxSendDelayMillis());
	}

	public void initializeUniverse() {
		if(olaClient == null) {
			// OLA client is not connected
			return;
		}
		
		logger.debug("Initializing DMX universe on OLA...");
		
		UniverseInfoReply universeInfoReply = olaClient.getUniverseList();

		if (universeInfoReply != null) {
			if (universeInfoReply.getUniverseCount() > 0) {
				// The default universe is already initialized
				return;
			}
		}

		DeviceInfoReply deviceInfoReply = olaClient.getDeviceInfo();

		if(deviceInfoReply != null) {
			// TODO
		}

		logger.debug("DMX universe on OLA initialized");
	}

}
