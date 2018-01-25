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
import com.ascargon.rocketshow.midi.MidiMapping;
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

	private MidiMapping midiMapping = new MidiMapping();

	private List<File> fileList = new ArrayList<File>();

	private Manager manager;

	private Timer autoStopTimer;

	private LocalDateTime lastStartTime;
	private long passedMillis;

	private boolean filesLoaded = false;

	// Is this the idle song?
	private boolean idleSong = false;

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
		synchronized (this) {
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
		if (!idleSong) {
			manager.stopIdleSong();
		}

		if (filesLoaded) {
			return;
		}

		playState = PlayState.LOADING;

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}

		logger.debug("Loading all files for song '" + name + "'...");

		for (File file : fileList) {
			if (file.isActive()) {
				if (file instanceof MidiFile) {
					MidiFile midiFile = (MidiFile) file;

					for (MidiRouting midiRouting : midiFile.getMidiRoutingList()) {
						midiRouting.getMidiMapping().setParent(midiMapping);
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
				int fileOffset = 0;

				if (file.isLoop()) {
					// At least one file is looped -> don't stop the song
					// automatically
					return;
				}

				if (file instanceof MidiFile) {
					fileOffset = ((MidiFile) file).getFullOffsetMillis();
				} else if (file instanceof AudioFile) {
					fileOffset = ((AudioFile) file).getFullOffsetMillis();
				} else if (file instanceof VideoFile) {
					fileOffset = ((VideoFile) file).getFullOffsetMillis();
				}

				if (file.getDurationMillis() + fileOffset > maxDurationAndOffset) {
					maxDurationAndOffset = file.getDurationMillis() + fileOffset;
				}
			}
		}

		maxDurationAndOffset -= passedMillis;
		
		logger.debug("Scheduled the auto-stop timer in " + maxDurationAndOffset + " millis");

		autoStopTimer = new Timer();
		autoStopTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					logger.debug("Automatically stopping the song...");
					
					autoStopTimer.cancel();
					autoStopTimer = null;

					if (autoStartNextSong && manager.getCurrentSetList().hasNextSong()) {
						// Stop, don't play the idle song but start playing the
						// next song
						manager.getCurrentSetList().nextSong(false);
						manager.getPlayer().play();
					} else {
						// Stop, play the idle song and select the next song
						// automatically (if there is one)
						manager.getCurrentSetList().nextSong();
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

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}
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

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}
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

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}
	}

	public synchronized void togglePlay() throws Exception {
		if (playState == PlayState.PLAYING) {
			stop();
		} else {
			play();
		}
	}

	public synchronized void stop(boolean playIdleSong) throws Exception {
		if (playState == PlayState.STOPPED || playState == PlayState.STOPPING) {
			return;
		}

		playState = PlayState.STOPPING;

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}

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

		if (!idleSong) {
			manager.getStateManager().notifyClients();
		}

		// Play the idle song, if necessary
		if (!idleSong && playIdleSong) {
			manager.playIdleSong();
		}
	}

	public synchronized void stop() throws Exception {
		stop(true);
	}

	@XmlTransient
	public MidiMapping getMidiMapping() {
		return midiMapping;
	}

	public void setMidiMapping(MidiMapping midiMapping) {
		this.midiMapping = midiMapping;
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

	@XmlTransient
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

	public void setPlayState(PlayState playState) {
		this.playState = playState;
	}

	@XmlTransient
	public boolean isIdleSong() {
		return idleSong;
	}

	public void setIdleSong(boolean idleSong) {
		this.idleSong = idleSong;
	}

	@XmlTransient
	public long getPassedMillis() {
		return passedMillis;
	}

	public void setPassedMillis(long passedMillis) {
		this.passedMillis = passedMillis;
	}

}
