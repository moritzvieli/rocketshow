package com.ascargon.rocketshow.song;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.midi.MidiRouting;
import com.ascargon.rocketshow.song.file.AudioFile;
import com.ascargon.rocketshow.song.file.File;
import com.ascargon.rocketshow.song.file.MidiFile;
import com.ascargon.rocketshow.song.file.VideoFile;;

@XmlRootElement
public class Song {

	final static Logger logger = Logger.getLogger(Song.class);

	public static final String FILE_EXTENSION = "sng";

	public enum PlayState {
		PLAYING, // Is the song playing?
		PAUSED, // Is the song paused?
		STOPPING, // Is the song being stopped?
		STOPPED, // Is the song stopped?
		LOADING // Is the song waiting for all files to be loaded to start
				// playing this song?
	}

	private PlayState playState = PlayState.STOPPED;

	private String name;

	private boolean autoStartNextSong = false;

	private String notes;

	private long durationMillis;

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private List<File> fileList = new ArrayList<File>();

	private Manager manager;

	private Timer autoStopTimer;

	private LocalDateTime lastStartTime;
	private long passedMillis;
	
	private boolean filesLoaded = false;

	public void close() throws Exception {
		for (File file : fileList) {
			file.close();
		}
		
		filesLoaded = false;

		// Cancel the auto-stop timer
		if (autoStopTimer != null) {
			autoStopTimer.cancel();
			autoStopTimer = null;
		}

		playState = PlayState.STOPPED;
	}

	public void playerLoaded() {
        synchronized(this){
            notifyAll();
       }
	}

	private boolean allFilesLoaded() {
		for (File file : fileList) {
			if (file.isActive() && !file.isLoaded()) {
				return false;
			}
		}

		return true;
	}

	// Load all files but don't start playing
	public synchronized void loadFiles() throws Exception {
		if(filesLoaded) {
			playState = PlayState.PAUSED;
			return;
		}
		
		playState = PlayState.LOADING;
		manager.getStateManager().notifyClients();
		
		logger.debug("Loading all files for song '" + name + "'...");

		for (File file : fileList) {
			if (file.isActive()) {
				if (file instanceof MidiFile) {
					MidiFile midiFile = (MidiFile) file;
					
					for(MidiRouting midiRouting : midiFile.getMidiRoutingList()) {
						midiRouting.getMidi2DmxMapping().setParent(midi2DmxMapping);
					}
				}

				file.setManager(manager);
				file.setSong(this);

				file.load();
			}
		}

		synchronized (this) {
			while (!allFilesLoaded()) {
				wait();
			}
		}
		
		logger.debug("All files for song '" + name + "' loaded");
		
		playState = PlayState.PAUSED;
		
		filesLoaded = true;
	}

	private void startAutoStopTimer(long passedMillis) {
		// Start the autostop timer, to automatically stop the song, as soon as
		// the last file (the longest one, which has the most offset) has been
		// finished)
		long maxDurationAndOffset = 0;

		for (File file : fileList) {
			if (file.isActive()) {
				if (file.getDurationMillis() + file.getOffsetMillis() > maxDurationAndOffset) {
					maxDurationAndOffset = file.getDurationMillis() + file.getOffsetMillis();
				}
			}
		}

		maxDurationAndOffset -= passedMillis;

		autoStopTimer = new Timer();
		autoStopTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					autoStopTimer = null;
					stop();

					if (autoStartNextSong) {
						int oldSongIndex = manager.getCurrentSetList().getCurrentSongIndex();
						manager.getCurrentSetList().nextSong();

						if (oldSongIndex != manager.getCurrentSetList().getCurrentSongIndex()) {
							// There really was a next song, it's not the
							// current one
							manager.getCurrentSetList().play();
						}
					}
				} catch (Exception e) {
					logger.error("Could not automatically stop song '" + name + "'", e);
				}
			}
		}, maxDurationAndOffset);
	}

	public synchronized void play() throws Exception {
		if (playState == PlayState.PLAYING || playState == PlayState.STOPPING || playState == PlayState.LOADING) {
			return;
		}

		// Load all files
		loadFiles();

		if (playState != PlayState.PAUSED) {
			// Maybe the song is stopping meanwhile
			return;
		}

		// All files are loaded -> play the song (start each file)
		logger.info("Playing song '" + name + "'");

		for (File file : fileList) {
			if (file.isActive()) {
				file.play();
			}
		}

		startAutoStopTimer(0);

		lastStartTime = LocalDateTime.now();
		passedMillis = 0;
		
		playState = PlayState.PLAYING;
		manager.getStateManager().notifyClients();
	}

	public synchronized void pause() throws Exception {
		// Cancel the auto-stop timer
		if (autoStopTimer != null) {
			autoStopTimer.cancel();
			autoStopTimer = null;
		}

		// Save the passed time since the last run
		passedMillis += lastStartTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);

		if (playState == PlayState.PAUSED) {
			return;
		}

		logger.info("Pausing song '" + name + "'");

		// Pause the song
		for (File file : fileList) {
			if (file.isActive()) {
				file.pause();
			}
		}

		playState = PlayState.PAUSED;
		manager.getStateManager().notifyClients();
	}

	public synchronized void resume() throws Exception {
		// Restart the auto-stop timer from the last pause
		startAutoStopTimer(passedMillis);
		lastStartTime = LocalDateTime.now();

		if (playState == PlayState.PLAYING) {
			return;
		}

		logger.info("Resuming song '" + name + "'");

		// Resume the song
		for (File file : fileList) {
			if (file.isActive()) {
				file.resume();
			}
		}

		playState = PlayState.PLAYING;
		manager.getStateManager().notifyClients();
	}

	public synchronized void togglePlay() throws Exception {
		if (playState == PlayState.PLAYING) {
			pause();
		} else {
			resume();
		}
	}

	public synchronized void stop() throws Exception {
		if (playState == PlayState.STOPPED || playState == PlayState.STOPPING) {
			return;
		}

		playState = PlayState.STOPPING;
		manager.getStateManager().notifyClients();
		
		// Cancel the auto-stop timer
		if (autoStopTimer != null) {
			autoStopTimer.cancel();
			autoStopTimer = null;
		}

		passedMillis = 0;

		logger.info("Stopping song '" + name + "'");

		// Stop the song
		for (File file : fileList) {
			if (file.isActive()) {
				try {
					file.close();
				} catch (Exception e) {
					logger.error("Could not stop file '" + file.getName() + "'");
				}
			}
		}
		
		filesLoaded = false;

		logger.info("Song '" + name + "' stopped");

		playState = PlayState.STOPPED;
		manager.getStateManager().notifyClients();
	}

	@XmlTransient
	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

	@XmlElementWrapper(name = "fileList")
	@XmlElements({ @XmlElement(type = MidiFile.class, name = "midiFile"),
			@XmlElement(type = VideoFile.class, name = "videoFile"),
			@XmlElement(type = AudioFile.class, name = "audioFile") })
	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
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

	@XmlTransient
	public PlayState getPlayState() {
		return playState;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	public boolean isAutoStartNextSong() {
		return autoStartNextSong;
	}

	public void setAutoStartNextSong(boolean autoStartNextSong) {
		this.autoStartNextSong = autoStartNextSong;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@XmlTransient
	public LocalDateTime getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(LocalDateTime lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public void setPlayState(PlayState playState) {
		this.playState = playState;
	}

}
