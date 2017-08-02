package com.ascargon.rocketshow;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.dmx.DmxSignalSender;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.image.ImageDisplayer;
import com.ascargon.rocketshow.midi.Startup;
import com.ascargon.rocketshow.song.SetList;
import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.video.VideoPlayer;

public class Manager {

	final static Logger logger = Logger.getLogger(Manager.class);
	
	public final String BASE_PATH = "/opt/rocketshow/";
	
	private DmxSignalSender dmxSignalSender;
	private Midi2DmxConverter midi2DmxConverter;
	
	private VideoPlayer videoPlayer;
	private ImageDisplayer imageDisplayer;
	
	private Session session = new Session();
	private Settings settings = new Settings();

	// Global settings
	private Midi2DmxMapping midi2DmxMapping;

	private SetList currentSetList;
	private Song currentSong;

	public void setSongIndex(int index) {
		if(currentSetList != null) {
			if(currentSetList.getSongList().size() >= index) {
				currentSong = currentSetList.getSongList().get(index);
				currentSetList.setCurrentSongIndex(index);
				
				logger.info("Set song index " + index);
			}
		}
	}
	
	public void loadSetlist(String path) {
		logger.info("Loading setlist " + path + "...");
		
		// Load a setlist
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			currentSetList = (SetList) jaxbUnmarshaller.unmarshal(new File(path));
			currentSetList.setManager(this);
			currentSetList.setPath(path);
			currentSetList.load();
			
			setSongIndex(0);
			
			logger.info("Setlist " + path + " successfully loaded");
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
		logger.info("Initialize RocketShow...");
		
		// Initialize the DMX sender and default global mapping
		dmxSignalSender = new DmxSignalSender();
		midi2DmxConverter = new Midi2DmxConverter(dmxSignalSender);
		initializeDefaultDmxMapping();
		
		// Initialize the video player
		videoPlayer = new VideoPlayer();
		
		// Initialize the image displayer
		imageDisplayer = new ImageDisplayer();

		loadSettings();
		restoreSession();
		
		logger.info("RocketShow initialized");
		
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
	
	public void loadSettings() {
		File file = new File(BASE_PATH + "settings");
		if(!file.exists() || file.isDirectory()) { 
		    return;
		}
		
		// Restore the session from the file
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			settings = (Settings) jaxbUnmarshaller.unmarshal(file);

			if(settings.getDefaultImagePath() != null) {
				imageDisplayer.display(settings.getDefaultImagePath());
			}
			
			logger.info("Settings loaded");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void saveSession() {
		if(currentSetList != null) {
			session.setCurrentSetListPath(currentSetList.getPath());
			session.setCurrentSongIndex(currentSetList.getCurrentSongIndex());
		}
		
		try {
			File file = new File(BASE_PATH + "session");
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(session, file);
			
			logger.info("Session saved");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void restoreSession() {
		File file = new File(BASE_PATH + "session");
		if(!file.exists() || file.isDirectory()) { 
		    return;
		}
		
		// Restore the session from the file
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			session = (Session) jaxbUnmarshaller.unmarshal(file);

			if(session.getCurrentSetListPath() != null) {
				loadSetlist(session.getCurrentSetListPath());
				
				if(session.getCurrentSongIndex() != null) {
					setSongIndex(session.getCurrentSongIndex());
				}
			}
			
			logger.info("Session restored");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
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

	public SetList getCurrentSetList() {
		return currentSetList;
	}

	public void setCurrentSetList(SetList currentSetList) {
		this.currentSetList = currentSetList;
	}

	public Song getCurrentSong() {
		return currentSong;
	}

	public void setCurrentSong(Song currentSong) {
		this.currentSong = currentSong;
	}

}
