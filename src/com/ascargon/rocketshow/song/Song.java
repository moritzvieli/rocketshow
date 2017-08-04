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

	private String path;

	private Midi2DmxMapping midi2DmxMapping = new Midi2DmxMapping();

	private List<File> fileList = new ArrayList<File>();

	private Manager manager;

	public void load() throws Exception {
		logger.info("Loading song " + path);
		
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);

			if (file instanceof MidiFile) {
				MidiFile midiFile = (MidiFile) file;
				midiFile.getMidi2DmxMapping().setParent(midi2DmxMapping);
			}

			file.setManager(manager);
			file.load();
		}
	}

	public void play() throws Exception {
		logger.info("Playing song " + path);
		
		// Start playing the song
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			file.play();
		}
	}

	public void pause() throws Exception {
		logger.info("Pausing song " + path);
		
		// Pause the song
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			file.pause();
		}
	}
	
	public void resume() throws Exception {
		logger.info("Resuming song " + path);
		
		// Pause the song
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			file.resume();
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

	@XmlTransient
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
