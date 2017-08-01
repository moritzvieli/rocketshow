package com.ascargon.rocketshow;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.ascargon.rocketshow.dmx.DmxSignalSender;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.Startup;
import com.ascargon.rocketshow.song.SetList;
import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.video.VideoPlayer;

public class Manager {

	private DmxSignalSender dmxSignalSender;
	private Midi2DmxConverter midi2DmxConverter;
	
	private VideoPlayer videoPlayer;

	// Global settings
	private Midi2DmxMapping midi2DmxMapping;

	private SetList currentSetList;
	private Song currentSong;

	public void loadSetlist(String path) {
		// Load a setlist
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			currentSetList = (SetList) jaxbUnmarshaller.unmarshal(new File(path));
			currentSetList.setManager(this);
			currentSetList.load();
			
			currentSong = currentSetList.getSongList().get(0);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeDefaultDmxMapping() {
		midi2DmxMapping = new Midi2DmxMapping();
		midi2DmxMapping.setChannelOffset(0);
		
		HashMap<Integer, Integer> channelMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < 128; i++) {
			channelMap.put(0, 0);	
		}
		
		midi2DmxMapping.setChannelMap(channelMap);
	}
	
	public void load() {
		// Initialize the DMX sender and default global mapping
		dmxSignalSender = new DmxSignalSender();
		midi2DmxConverter = new Midi2DmxConverter(dmxSignalSender);
		initializeDefaultDmxMapping();
		
		// Initialize the video player
		videoPlayer = new VideoPlayer();

		// TODO Load the last setlist stored in the session object
		
		// TODO Initialize the MIDI system
		Startup s = new Startup();
		String[] args = new String[1];
		args[0] = "-l";
		try {
			s.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		currentSong.play();
	}
	
	public void pause() {
		currentSong.pause();
	}

	public String test() {
		return "Hello there";
	}

	public Midi2DmxConverter getMidi2DmxConverter() {
		return midi2DmxConverter;
	}

	public void setMidi2DmxConverter(Midi2DmxConverter midi2DmxConverter) {
		this.midi2DmxConverter = midi2DmxConverter;
	}

	public VideoPlayer getVideoPlayer() {
		return videoPlayer;
	}

	public void setVideoPlayer(VideoPlayer videoPlayer) {
		this.videoPlayer = videoPlayer;
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

}
