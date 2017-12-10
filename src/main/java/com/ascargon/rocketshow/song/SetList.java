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

	private Song currentSong;
	
	private ExecutorService playExecutor;

	// Read the current song from its file
	public void readCurrentSong() throws Exception {
		if (currentSongIndex >= setListSongList.size()) {
			return;
		}

		// Stop the current song (if not already done)
		if (currentSong != null) {
			currentSong.stop();
		}

		currentSong = manager.getSongManager().loadSong(setListSongList.get(currentSongIndex).getName());
		currentSong.setName(setListSongList.get(currentSongIndex).getName());
		currentSong.getMidiMapping().setParent(manager.getSettings().getMidiMapping());
		currentSong.setManager(manager);
	}

	// Return only the setlist-relevant information of the song (e.g. to save to
	// a file)
	@XmlElement(name = "song")
	@XmlElementWrapper(name = "songList")
	public List<SetListSong> getSetListSongList() {
		return setListSongList;
	}

	public void load() throws Exception {
		if (currentSong != null) {
			currentSong.loadFiles();
		}
	}

	public void play() throws Exception {
		currentSong.setPlayState(PlayState.LOADING);
		
		// Make sure all remote devices and the local one have loaded the song
		// before playing it
		playExecutor = Executors.newFixedThreadPool(30);

		// Load all remote devices song
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				playExecutor.execute(new Runnable() {
					public void run() {
						remoteDevice.load(true);
					}
				});
			}
		}

		// Also load the local song files
		if (currentSong != null) {
			playExecutor.execute(new Runnable() {
				public void run() {
					try {
						currentSong.loadFiles();
					} catch (Exception e) {
						logger.error("Could not load the song files", e);
					}
				}
			});
		}

		logger.debug("Wait for all devices to be loaded...");
		
		// Wait for the songs on all devices to be loaded
		playExecutor.shutdown();

		while (!playExecutor.isTerminated()) {
		}

		logger.debug("All devices loaded");
		
		if (currentSong.getPlayState() != PlayState.PAUSED) {
			// Maybe the song stopped meanwhile
			return;
		}
		
		logger.debug("Start playing on all devices...");
		
		// Play the song on all remote devices
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				remoteDevice.play();
			}
		}

		// Play the song locally
		if (currentSong != null) {
			currentSong.play();
		}
		
		logger.debug("Playing on all devices");
	}

	public void pause() throws Exception {
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				remoteDevice.pause();
			}
		}

		if (currentSong != null) {
			currentSong.pause();
		}
	}

	public void resume() throws Exception {
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				remoteDevice.resume();
			}
		}

		if (currentSong != null) {
			currentSong.resume();
		}
	}

	public void togglePlay() throws Exception {
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				remoteDevice.togglePlay();
			}
		}

		if (currentSong != null) {
			currentSong.togglePlay();
		}
	}

	public void stop() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(30);

		// Reset the DMX universe to clear left out signals
		manager.getDmxSignalSender().reset();
		
		// Stop all remote devices
		for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				executor.execute(new Runnable() {
					public void run() {
						remoteDevice.stop();
					}
				});
			}
		}

		// Also load the local song files
		if (currentSong != null) {
			executor.execute(new Runnable() {
				public void run() {
					try {
						currentSong.stop();
					} catch (Exception e) {
						logger.error("Could not load the song files", e);
					}
				}
			});
		}

		// Wait for all devices to be stopped
		executor.shutdown();

		while (!executor.isTerminated()) {
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
		if (setListSongList.size() == 0) {
			return null;
		}

		return setListSongList.get(currentSongIndex).getName();
	}

	public void setCurrentSongIndex(int currentSongIndex) throws Exception {
		if (manager != null) {
			for (RemoteDevice remoteDevice : manager.getSettings().getRemoteDeviceList()) {
				if (remoteDevice.isSynchronize()) {
					remoteDevice.setSongIndex(currentSongIndex);
				}
			}
		}

		this.currentSongIndex = currentSongIndex;
		readCurrentSong();

		logger.info("Set song index " + currentSongIndex);
	}

	public Song getCurrentSong() {
		return currentSong;
	}

}
