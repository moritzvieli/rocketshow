package com.ascargon.rocketshow.song;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
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

	private PlayState playState;

	private String name;

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private List<File> fileList = new ArrayList<File>();

	private Manager manager;

	public void load() throws Exception {
		logger.info("Loading song '" + name + "'");

		// Load all files inside the song
		for (File file : fileList) {
			if (file.isActive()) {
				if (file instanceof MidiFile) {
					MidiFile midiFile = (MidiFile) file;
					midiFile.getMidiRouting().getMidi2DmxMapping().setParent(midi2DmxMapping);
				}

				file.setManager(manager);
				file.setSong(this);
				file.load();
			}
		}
	}

	public void close() throws Exception {
		for (File file : fileList) {
			file.close();
		}

		playState = PlayState.STOPPED;
	}

	public void playerLoaded() {
		// Start playing the song, if needed
		if (playState == PlayState.LOADING) {
			try {
				play();
			} catch (Exception e) {
				logger.error("Could not play song '" + name + "'", e);
			}
		}
	}

	public synchronized void play() throws Exception {
		if (playState == PlayState.PLAYING || playState == PlayState.STOPPING) {
			return;
		}

		playState = PlayState.LOADING;

		boolean allFilesLoaded = true;

		// Only play, if all files have been finished loading
		for (File file : fileList) {
			if (!file.isLoaded() && file.isActive()) {
				// This song is not yet loaded -> start playing, as soon as all
				// files have been loaded
				logger.debug("File '" + file.getName() + "' not yet loaded");
				allFilesLoaded = false;

				if (!file.isLoading()) {
					file.load();
				}
			}
		}

		if (!allFilesLoaded) {
			return;
		}

		if (playState != PlayState.LOADING) {
			// Maybe stopping meanwhile
			return;
		}

		logger.info("Playing song '" + name + "'");

		for (File file : fileList) {
			if (file.isActive()) {
				file.play();
			}
		}

		playState = PlayState.PLAYING;

		manager.getStateManager().notifyClients();
	}

	public synchronized void pause() throws Exception {
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

		logger.info("Stopping song '" + name + "'");

		// Stop the song
		for (File file : fileList) {
			if (file.isActive()) {
				try {
					file.stop();
				} catch (Exception e) {
					logger.error("Could not stop file '" + file.getName() + "'");
				}
			}
		}

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

}
