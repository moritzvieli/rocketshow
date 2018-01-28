package com.ascargon.rocketshow.song;

import java.util.ArrayList;
import java.util.List;

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

	private int currentSongIndex;

	private Manager manager;

	private String notes;
	
	public SetList() {
		currentSongIndex = 0;
	}

	// Read the current song from its file
	public void readCurrentSong() throws Exception {
		if (currentSongIndex >= setListSongList.size()) {
			return;
		}

		// Load the current song into the player
		SetListSong currentSetListSong = setListSongList.get(currentSongIndex);
		
		manager.getPlayer().setCurrentSong(manager.getSongManager().loadSong(currentSetListSong.getName()));
		manager.getPlayer().setAutoStartNextSong(currentSetListSong.isAutoStartNextSong());
	}

	// Return only the setlist-relevant information of the song (e.g. to save to
	// a file)
	@XmlElement(name = "song")
	@XmlElementWrapper(name = "songList")
	public List<SetListSong> getSetListSongList() {
		return setListSongList;
	}

	public void nextSong(boolean playIdleSong) throws Exception {
		int newIndex = currentSongIndex + 1;

		if (newIndex >= setListSongList.size()) {
			return;
		}

		setSongIndex(newIndex, playIdleSong);
	}

	public boolean hasNextSong() {
		int newIndex = currentSongIndex + 1;

		if (newIndex >= setListSongList.size()) {
			return false;
		}

		return true;
	}

	public void nextSong() throws Exception {
		nextSong(true);
	}

	public void previousSong() throws Exception {
		int newIndex = currentSongIndex - 1;

		if (newIndex < 0) {
			return;
		}

		setSongIndex(newIndex);
	}

	public void close() throws Exception {
		// Nothing to do at the moment
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

	@XmlElement
	public int getCurrentSongIndex() {
		return currentSongIndex;
	}
	
	// For the XML load to work
	public void setCurrentSongIndex(int currentSongIndex) throws Exception {
		this.setSongIndex(currentSongIndex);
	}

	@XmlTransient
	public String getCurrentSongName() {
		if (setListSongList.size() == 0) {
			return null;
		}

		return setListSongList.get(currentSongIndex).getName();
	}

	public void setSongIndex(int songIndex, boolean playIdleSong) throws Exception {
		// Stop a playing song if needed
		if (manager != null) {
			manager.getPlayer().stop(playIdleSong);
		}
		
		// Return, if we already have the correct song set
		if (currentSongIndex == songIndex) {
			return;
		}

		currentSongIndex = songIndex;
		
		// Load the new song
		readCurrentSong();

		if (manager != null) {
			if (manager.getStateManager() != null) {
				manager.getStateManager().notifyClients();
			}
		}

		if (manager != null) {
			// Save the set list to remember the current song index (e.g. after
			// a reboot)
			manager.getSongManager().saveSetList(this, false);
		}

		logger.info("Set song index " + currentSongIndex);
	}

	public void setSongIndex(int songIndex) throws Exception {
		setSongIndex(songIndex, true);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
