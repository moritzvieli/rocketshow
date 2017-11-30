package com.ascargon.rocketshow.song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

public class SongManager {

	final static Logger logger = Logger.getLogger(SongManager.class);

	public final static String SONG_PATH = "song/";
	public final static String SETLIST_PATH = "setlist/";

	public SongManager() {
	}

	public List<Song> getAllSongs() throws Exception {
		File folder = new File(Manager.BASE_PATH + SONG_PATH);
		File[] fileList = folder.listFiles();
		List<Song> songList = new ArrayList<Song>();

		for (File file : fileList) {
			if (file.isFile()) {
				Song song = new Song();
				song.setName(file.getName());
				
				songList.add(song);
			}
		}
		
		return songList;
	}
	
	public List<SetList> getAllSetLists() throws Exception {
		File folder = new File(Manager.BASE_PATH + SETLIST_PATH);
		File[] fileList = folder.listFiles();
		List<SetList> setListList = new ArrayList<SetList>();

		for (File file : fileList) {
			if (file.isFile()) {
				SetList setList = new SetList();
				setList.setName(file.getName());
				
				setListList.add(setList);
			}
		}
		
		return setListList;
	}

	public SetList loadSetList(String name) throws Exception {
		SetList setList;

		logger.info("Loading setlist '" + name + "'...");

		// Load a setlist
		JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		setList = (SetList) jaxbUnmarshaller.unmarshal(new File(Manager.BASE_PATH + SETLIST_PATH + name));

		logger.info("Setlist '" + name + "' successfully loaded");

		return setList;
	}

	public Song loadSong(String name) throws Exception {
		Song song;

		logger.info("Loading song " + name + "...");

		// Load a setlist
		JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		song = (Song) jaxbUnmarshaller.unmarshal(new File(Manager.BASE_PATH + SONG_PATH + name));

		logger.info("Song '" + name + "' successfully loaded");

		return song;
	}

	public void saveSetList(SetList setList) throws JAXBException {
		File file = new File(Manager.BASE_PATH + SETLIST_PATH + setList.getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(setList, file);

		logger.info("Setlist '" + setList.getName() + "' saved");
	}

	public void saveSong(Song song) throws JAXBException {
		// Get the duration of each file
		ExecutorService executor = Executors.newFixedThreadPool(30);

		for (com.ascargon.rocketshow.song.file.File file : song.getFileList()) {
			Runnable fileDurationGetter = new FileDurationGetter(file);
			executor.execute(fileDurationGetter);
		}

		executor.shutdown();

		// Wait until all threads are finish
		while (!executor.isTerminated()) {
		}

		// Set the duration of the song to the maximum duration of the files
		long maxDuration = 0;

		for (com.ascargon.rocketshow.song.file.File file : song.getFileList()) {
			if (file.getDurationMillis() > maxDuration) {
				maxDuration = file.getDurationMillis();
			}
		}
		song.setDurationMillis(maxDuration);

		// Save the song in XML
		File file = new File(Manager.BASE_PATH + SONG_PATH + song.getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(song, file);

		logger.info("Song '" + song.getName() + "' saved");
	}

}
