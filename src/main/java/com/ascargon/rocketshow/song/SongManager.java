package com.ascargon.rocketshow.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.util.FileDurationGetter;

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

		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					Song song = new Song();
					song.setName(file.getName());

					songList.add(song);
				}
			}
		}

		return songList;
	}

	public List<SetList> getAllSetLists() throws Exception {
		File folder = new File(Manager.BASE_PATH + SETLIST_PATH);
		File[] fileList = folder.listFiles();
		List<SetList> setListList = new ArrayList<SetList>();

		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					SetList setList = new SetList();
					setList.setName(file.getName());

					setListList.add(setList);
				}
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

		// Load a song
		JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		song = (Song) jaxbUnmarshaller.unmarshal(new File(Manager.BASE_PATH + SONG_PATH + name));

		logger.info("Song '" + name + "' successfully loaded");

		return song;
	}

	public void saveSetList(SetList setList) throws Exception {
		// Update all song information
		Iterator<SetListSong> iterator = setList.getSetListSongList().iterator();

		while (iterator.hasNext()) {
			SetListSong setListSong = iterator.next();

			Song song = null;

			try {
				song = loadSong(setListSong.getName());
			} catch (Exception e) {
			}

			if (song == null) {
				// The song does not exist anymore (has been deleted)
				// --> delete it from the setList
				iterator.remove();
			} else {
				// The song still exists -> update some information
				setListSong.setDurationMillis(song.getDurationMillis());
			}
		}

		File file = new File(Manager.BASE_PATH + SETLIST_PATH + setList.getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(setList, file);

		logger.info("Setlist '" + setList.getName() + "' saved");
	}

	public void saveSong(Song song) throws Exception {
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

		updateSetLists();

		logger.info("Song '" + song.getName() + "' saved");
	}

	public void deleteSong(String name) throws Exception {
		// Delete the song
		File file = new File(Manager.BASE_PATH + SONG_PATH + name);

		if (!file.exists()) {
			updateSetLists();
			return;
		} else {
			file.delete();
			updateSetLists();
		}

		// TODO What do we do, if this is the current song?

		logger.info("Song '" + name + "' deleted");
	}
	
	public void deleteSetList(String name) throws Exception {
		// Delete the setlist
		File file = new File(Manager.BASE_PATH + SETLIST_PATH + name);

		if (file.exists()) {
			file.delete();
		}

		logger.info("SetList '" + name + "' deleted");
	}

	private void updateSetLists() throws Exception {
		// Update all setlists (remove deleted files, update playing times),
		// when a song has been changed/deleted

		List<SetList> setLists = getAllSetLists();

		for (SetList setList : setLists) {
			// Load the full setlist
			SetList fullSetList = loadSetList(setList.getName());
			
			saveSetList(fullSetList);
		}
	}

}
