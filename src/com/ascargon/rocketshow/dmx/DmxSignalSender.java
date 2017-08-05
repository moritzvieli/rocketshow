package com.ascargon.rocketshow.dmx;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.midi.MidiReceiver;
import com.ascargon.rocketshow.util.ShellManager;

public class DmxSignalSender {

	final static Logger logger = Logger.getLogger(MidiReceiver.class);
	
	final String URL  = "http://localhost:9090/set_dmx";
	
	private ShellManager shellManager;
	
	// Cache the channel values and send them each time
	private HashMap<Integer, Integer> channelValues = new HashMap<Integer, Integer>();
	
	public DmxSignalSender() {
		try {
			shellManager = new ShellManager();
		} catch (IOException e) {
			logger.debug("Could not initialize ShellManager for DmxSignalSender ", e);
		}
		
		for (int i = 0; i < 512; i++) {
			channelValues.put(i, 0);
		}
		
		// Initialize the universe
		try {
			sendUniverse();
		} catch (IOException e) {
			logger.debug("Could not initialize the DMX universe", e);
		}
	}
	
	private void sendUniverse() throws IOException {
		String universe = "";
		
		for (int i = 0; i < channelValues.size(); i++) {
			universe += channelValues.get(i) + ",";
		}
		
		// Cut away the last delimiter
		universe.substring(0, universe.length() - 1);
		
		if(shellManager != null) {
			shellManager.sendCommand("curl -d \"u=1&d=" + universe + "\" -X POST " + URL);
		}
	}
	
    public void send(int channel, int value) throws IOException {
    		logger.debug("Setting DMX channel " + channel + " to value " + value);
    		
    		channelValues.put(channel, value);
    		
    		sendUniverse();
    }

}
