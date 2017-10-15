package com.ascargon.rocketshow.song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;

@XmlRootElement
public class SetList {

	final static Logger logger = Logger.getLogger(SetList.class);
	
	public static final String FILE_EXTENSION = "stl";

	private String path;

	private List<SetListSong> setListSongList = new ArrayList<SetListSong>();

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private int currentSongIndex = 0;

	private Manager manager;
	
	private Song currentSong;

	// Load all songs inside the setlist
	public void load() throws Exception {
		midi2DmxMapping.setParent(manager.getSettings().getFileMidi2DmxMapping());
	}

	// Return only the setlist-relevant information of the song (e.g. to save to
	// a file)
	@XmlElement(name = "song")
	@XmlElementWrapper(name = "songList")
	public List<SetListSong> getSetListSongList() {
		return setListSongList;
	}

	public void setSongIndex(int index) {
		currentSongIndex = index;
		logger.info("Set song index " + index);
	}
	
	public void play() throws Exception {
		// Load the song first
		File file = new File(setListSongList.get(currentSongIndex).getPath());
		JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		currentSong = (Song)jaxbUnmarshaller.unmarshal(file);
		currentSong.setPath(setListSongList.get(currentSongIndex).getPath());
		currentSong.getMidi2DmxMapping().setParent(midi2DmxMapping);
		currentSong.setManager(manager);
		currentSong.load();
		
		currentSong.play();
	}

	public void pause() throws Exception {
		currentSong.stop();
	}
	
	public void resume() throws Exception {
		currentSong.resume();
	}
	
	public void togglePlay() throws Exception {
		currentSong.togglePlay();
	}
	
	public void stop() throws Exception {
		currentSong.stop();
	}
	
	public void nextSong() throws Exception {
		// TODO
	}
	
	public void previousSong() throws Exception {
		// TODO
	}
	
	public void close() {
		// TODO
	}
	
	public void setXmlSongList(List<SetListSong> setListSongList) {
		this.setListSongList = setListSongList;
	}
	
	@XmlTransient
	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

	@XmlTransient
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlTransient
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@XmlTransient
	public int getCurrentSongIndex() {
		return currentSongIndex;
	}

	public void setCurrentSongIndex(int currentSongIndex) {
		this.currentSongIndex = currentSongIndex;
	}

}
