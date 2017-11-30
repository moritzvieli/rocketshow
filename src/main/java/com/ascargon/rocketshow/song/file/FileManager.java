package com.ascargon.rocketshow.song.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ascargon.rocketshow.Manager;

public class FileManager {

	public List<com.ascargon.rocketshow.song.file.File> getAllFiles() throws Exception {
		List<com.ascargon.rocketshow.song.file.File> returnFileList = new ArrayList<com.ascargon.rocketshow.song.file.File>();
		File folder;
		File[] fileList;
		
		// Audio files
		folder = new File(
				Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + AudioFile.AUDIO_PATH);
		fileList = folder.listFiles();
		
		for (File file : fileList) {
			if (file.isFile()) {
				AudioFile audioFile = new AudioFile();
				audioFile.setName(file.getName());
				returnFileList.add(audioFile);
			}
		}
		
		// MIDI files
		folder = new File(
				Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + MidiFile.MIDI_PATH);
		fileList = folder.listFiles();
		
		for (File file : fileList) {
			if (file.isFile()) {
				MidiFile midiFile = new MidiFile();
				midiFile.setName(file.getName());
				returnFileList.add(midiFile);
			}
		}
		
		// Video files
		folder = new File(
				Manager.BASE_PATH + com.ascargon.rocketshow.song.file.File.MEDIA_PATH + VideoFile.VIDEO_PATH);
		fileList = folder.listFiles();
		
		for (File file : fileList) {
			if (file.isFile()) {
				VideoFile videoFile = new VideoFile();
				videoFile.setName(file.getName());
				returnFileList.add(videoFile);
			}
		}

		return returnFileList;
	}

}
