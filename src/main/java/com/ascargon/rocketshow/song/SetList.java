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

@XmlRootElement
public class SetList {

	final static Logger logger = Logger.getLogger(SetList.class);

	public static final String FILE_EXTENSION = "stl";

	private String name;

	private List<SetListSong> setListSongList = new ArrayList<SetListSong>();

	private int currentSongIndex = 0;

	private Manager manager;

	private Song currentSong;

	private boolean loaded = false;

	// Preload the current song
	public void load() throws Exception {
		if (currentSongIndex >= setListSongList.size()) {
			return;
		}

		// Stop a current song, if required
		if (currentSong != null) {
			if (currentSong.getPlayState() == Song.PlayState.PLAYING
					|| currentSong.getPlayState() == Song.PlayState.PAUSED) {
				
				currentSong.stop();
			}
		}

		// Load the song first
		File file = new File(Manager.BASE_PATH + "song/" + setListSongList.get(currentSongIndex).getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		currentSong = (Song) jaxbUnmarshaller.unmarshal(file);
		currentSong.setName(setListSongList.get(currentSongIndex).getName());
		currentSong.getMidi2DmxMapping().setParent(manager.getSettings().getMidi2DmxMapping());
		currentSong.setManager(manager);

		currentSong.load();
		loaded = true;
	}

	// Return only the setlist-relevant information of the song (e.g. to save to
	// a file)
	@XmlElement(name = "song")
	@XmlElementWrapper(name = "songList")
	public List<SetListSong> getSetListSongList() {
		return setListSongList;
	}

	public void play() throws Exception {
		if (!loaded) {
			load();
		}

		if (currentSong != null) {
			currentSong.play();
		}
	}

	public void pause() throws Exception {
		if (!loaded) {
			load();
		}

		if (currentSong != null) {
			currentSong.pause();
		}
	}

	public void resume() throws Exception {
		if (!loaded) {
			load();
		}

		if (currentSong != null) {
			currentSong.resume();
		}
	}

	public void togglePlay() throws Exception {
		if (!loaded) {
			load();
		}

		if (currentSong != null) {
			currentSong.togglePlay();
		}
	}

	public void stop() throws Exception {
		if (!loaded) {
			load();
		}

		if (currentSong != null) {
			currentSong.stop();
		}
	}

	public void nextSong() throws Exception {
		int newIndex = currentSongIndex + 1;

		if (newIndex >= setListSongList.size()) {
			return;
		}

		if (currentSong != null) {
			currentSong.close();
		}

		setCurrentSongIndex(newIndex);
		
		manager.getStateManager().notifyClients();
	}

	public void previousSong() throws Exception {
		int newIndex = currentSongIndex - 1;

		if (newIndex < 0) {
			return;
		}

		if (currentSong != null) {
			currentSong.close();
		}

		setCurrentSongIndex(newIndex);
		
		manager.getStateManager().notifyClients();
	}

	public void close() throws Exception {
		if (currentSong != null) {
			currentSong.close();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public int getCurrentSongIndex() {
		return currentSongIndex;
	}

	public String getCurrentSongName() {
		return setListSongList.get(currentSongIndex).getName();
	}

	public void setCurrentSongIndex(int currentSongIndex) throws Exception {
		this.currentSongIndex = currentSongIndex;
		logger.info("Set song index " + currentSongIndex);
		loaded = false;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public Song getCurrentSong() {
		return currentSong;
	}

}
