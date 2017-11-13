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

	private String name;

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private List<File> fileList = new ArrayList<File>();

	private Manager manager;

	// Waiting for all files to be loaded to start playing this song?
	private boolean loading = false;

	// Is this song playing?
	private boolean playing = false;

	public void load() throws Exception {
		logger.info("Loading song '" + name + "'");

		// Load all files inside the song
		for (File file : fileList) {
			if (file instanceof MidiFile) {
				MidiFile midiFile = (MidiFile) file;
				midiFile.getMidiRouting().getMidi2DmxMapping().setParent(midi2DmxMapping);
			}

			file.setManager(manager);
			file.setSong(this);
			file.load();
		}
	}

	public void close() throws Exception {
		loading = false;
		playing = false;

		for (File file : fileList) {
			file.close();
		}
	}

	public void playerLoaded() {
		// Start playing the song, if needed
		if (loading) {
			try {
				play();
			} catch (Exception e) {
				logger.error("Could not play song '" + name + "'", e);
			}
		}
	}

	public void play() throws Exception {
		boolean allFilesLoaded = true;
		
		if (playing) {
			return;
		}

		loading = true;

		// Only play, if all files have been finished loading
		for (File file : fileList) {
			if (!file.isLoaded() && file.isActive()) {
				// This song is not yet loaded -> start playing, as soon as all
				// files have been loaded
				logger.debug("File '" + file.getName() + "' not yet loaded");
				allFilesLoaded = false;
				
				if(!file.isLoading()) {
					file.load();
				}
			}
		}
		
		if(!allFilesLoaded) {
			return;
		}

		loading = false;
		playing = true;

		logger.info("Playing song '" + name + "'");

		for (File file : fileList) {
			if(file.isActive()) {
				file.play();
			}
		}
	}

	public void pause() throws Exception {
		logger.info("Pausing song '" + name + "'");

		playing = false;

		// Pause the song
		for (File file : fileList) {
			if(file.isActive()) {
				file.pause();
			}
		}
	}

	public void resume() throws Exception {
		logger.info("Resuming song '" + name + "'");

		playing = true;

		// Resume the song
		for (File file : fileList) {
			if(file.isActive()) {
				file.resume();
			}
		}
	}

	public void togglePlay() throws Exception {
		if (playing) {
			pause();
		} else {
			resume();
		}
	}

	public void stop() throws Exception {
		logger.info("Stopping song '" + name + "'");

		playing = false;

		// Stop the song
		for (File file : fileList) {
			if(file.isActive()) {
				file.stop();
			}
		}
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
	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

}
