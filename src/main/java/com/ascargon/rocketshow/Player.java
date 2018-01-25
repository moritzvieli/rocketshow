package com.ascargon.rocketshow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.song.Song.PlayState;

public class Player {
	
	final static Logger logger = Logger.getLogger(Player.class);

	private Song currentSong;
	private Manager manager;
	
	public Player(Manager manager) {
		this.manager = manager;
	}
	
	public void load() throws Exception {
		if (currentSong != null) {
			currentSong.loadFiles();
		}
	}
	
	public void play() throws Exception {
		ExecutorService playExecutor;
		
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

		// Also stop the local song
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
	
	public void close() throws Exception {
		if (currentSong != null) {
			currentSong.close();
		}
	}
	
	public Song getCurrentSong() {
		return currentSong;
	}

	public void setCurrentSong(Song currentSong) {
		this.currentSong = currentSong;
	}
	
}
