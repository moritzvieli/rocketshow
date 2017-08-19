package com.ascargon.rocketshow.dmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.midi.MidiReceiver;

public class DmxSignalSender {

	final static Logger logger = Logger.getLogger(MidiReceiver.class);

	private Manager manager;
	
	private final String URL = "http://localhost:9090/set_dmx";

	// Cache the channel values and send them each time
	private HashMap<Integer, Integer> channelValues;

	private HttpClient httpClient;

	// Delay sending of the universe because of 2 reasons:
	// - Performance: Sending the whole universe each midi event is not fast enough
	// - Glitches: If we send each event separately, you can see the transitions even if
	//   they're not meant to be (e.g. activate two channels at the same time, but sent
	//   separately)
	private Timer timer;
	private int executedCount = 0;
	
	public DmxSignalSender(Manager manager) {
		this.manager = manager;
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		
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
			logger.debug("Could not initialize the DMX universe", e);
		}
	}

	private void sendUniverse() throws IOException {
		String universe = "";

		for (int i = 0; i < channelValues.size(); i++) {
			universe += channelValues.get(i) + ",";
		}

		// Cut away the last delimiter
		universe = universe.substring(0, universe.length() - 1);

		// Post the data to the OLA backend API
		HttpPost httpPost = new HttpPost(URL);
		List<NameValuePair> data = new ArrayList<NameValuePair>(2);
		data.add(new BasicNameValuePair("u", "1"));
		data.add(new BasicNameValuePair("d", universe));
		httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		HttpResponse response = httpClient.execute(httpPost);

		// Read the response. The POST connection will not be released otherwise
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        
        String line = "";
        
        while ((line = rd.readLine()) != null) {
            logger.debug("Response from OLA POST: " + line);
        }
	}
	
	public void send(int channel, int value) throws IOException {
		logger.debug("Setting DMX channel " + channel + " to value " + value);

		channelValues.put(channel, value);
		
		// Schedule the specified count of timers in the specified delay
		if(timer == null) {
			
			TimerTask timerTask = new TimerTask() {
		        @Override
		        public void run() {
			    		try {
			    			// Send the universe and reset the timer for the next run
			    			sendUniverse();
			    		} catch (IOException e) {
			    			logger.error("Could not send the DMX universe", e);
			    		}
			    		
			    		executedCount ++;
			    		
			    		if(executedCount >= manager.getSettings().getDmxSendRepeat()) {
			    			timer.cancel();
				    		timer = null;
			    		}
		        }
		    };

		 	timer = new Timer();
			timer.scheduleAtFixedRate(timerTask, manager.getSettings().getDmxSendDelayMillis(), manager.getSettings().getDmxSendDelayMillis());	
		}
	}

}
