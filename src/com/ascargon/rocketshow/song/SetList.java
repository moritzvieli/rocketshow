package com.ascargon.rocketshow.song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;

@XmlRootElement
public class SetList {
	
	public static final String FILE_EXTENSION = "stl";
	
	private String path;
	
	private List<Song> songList = new ArrayList<Song>();
	
	private List<SetListSong> setListSongList = new ArrayList<SetListSong>();
	
	private Midi2DmxMapping midi2DmxMapping;

	private Manager manager;
	
	// Load all songs inside the setlist
	public void load() {
		midi2DmxMapping.setParent(manager.getMidi2DmxMapping());
		
		songList = new ArrayList<Song>();

		for (int i = 0; i < setListSongList.size(); i++) {
			String path = setListSongList.get(i).getPath();
			
			try {
				File file = new File(path);
				JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Song song = (Song) jaxbUnmarshaller.unmarshal(file);
				song.setPath(path);
				song.getMidi2DmxMapping().setParent(midi2DmxMapping);
				song.load();
				song.setManager(manager);
				songList.add(song);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Return only the setlist-relevant information of the song (e.g. to save to a file)
	@XmlElement(name = "song")
	@XmlElementWrapper(name="songList")
	public List<SetListSong> getSetListSongList() {
		setListSongList = new ArrayList<SetListSong>();
		
		for (int i = 0; i < songList.size(); i++) {
			SetListSong setListSong = new SetListSong();
			setListSong.create(songList.get(i));
			
			setListSongList.add(setListSong);
		}
		
		return setListSongList;
	}

	public void setXmlSongList(List<SetListSong> setListSongList) {
		this.setListSongList = setListSongList;
	}
	
	@XmlTransient
	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	@XmlTransient
	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
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
