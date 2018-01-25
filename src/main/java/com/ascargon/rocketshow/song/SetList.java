package com.ascargon.rocketshow.song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.RemoteDevice;
import com.ascargon.rocketshow.song.Song.PlayState;

@XmlRootElement
public class SetList {

	final static Logger logger = Logger.getLogger(SetList.class);

	public static final String FILE_EXTENSION = "stl";

	private String name;

	private List<SetListSong> setListSongList = new ArrayList<SetListSong>();

	private int currentSongIndex = 0;

	private Manager manager;

	private String notes;

	// Read the current song from its file
	public void readCurrentSong() throws Exception {
		if (currentSongIndex >= setListSongList.size()) {
			return;
		}

		SetListSong currentSetListSong = setListSongList.get(currentSongIndex);

		// Stop the current song (if not already done)
		if (manager.getPlayer().getCurrentSong() != null) {
			manager.getPlayer().getCurrentSong().stop();
		}
		
		Song currentSong = manager.getSongManager().loadSong(currentSetListSong.getName());

		manager.getPlayer().setCurrentSong(currentSong);
		currentSong.setName(currentSetListSong.getName());
		currentSong.getMidiMapping().setParent(manager.getSettings().getMidiMapping());
		currentSong.setManager(manager);
		currentSong.setAutoStartNextSong(currentSetListSong.isAutoStartNextSong());
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

		if (manager.getPlayer().getCurrentSong() != null) {
			manager.getPlayer().getCurrentSong().close();
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

		if (manager.getPlayer().getCurrentSong() != null) {
			manager.getPlayer().getCurrentSong().close();
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

	public int getCurrentSongIndex() {
		return currentSongIndex;
	}

	@XmlTransient
	public String getCurrentSongName() {
		if (setListSongList.size() == 0) {
			return null;
		}

		return setListSongList.get(currentSongIndex).getName();
	}

	public void setSongIndex(int songIndex, boolean playIdleSong) throws Exception {
		// Stop a playing song if needed and wait until it is stopped
		if (manager.getPlayer().getCurrentSong() != null) {
			logger.info(manager.getPlayer().getCurrentSong().getPlayState());
			
			if(manager.getPlayer().getCurrentSong().getPlayState() != PlayState.STOPPED) {
				manager.getPlayer().stop();
				
				while (manager.getPlayer().getCurrentSong().getPlayState() != PlayState.STOPPED) {
					Thread.sleep(50);
				}
			}
		}

		// Return, if we already have the correct song set
		if (currentSongIndex == songIndex) {
			return;
		}

		// Load the new song
		currentSongIndex = songIndex;
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
