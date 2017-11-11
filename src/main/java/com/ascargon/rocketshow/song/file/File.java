package com.ascargon.rocketshow.song.file;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.Song;

@XmlRootElement
abstract public class File implements PlayerLoadedListener {

	private String path;
	
	private Manager manager;
	private Song song;
	
	private boolean loaded = false;

	// Play offset
	private int offsetInMillis = 0;
	
	abstract public void load() throws Exception;
	
	abstract public void close() throws Exception;
	
	abstract public void play() throws Exception;
	
	abstract public void pause() throws Exception;
	
	abstract public void resume() throws Exception;
	
	abstract public void stop() throws Exception;
	
	@Override
	public void playerLoaded() {
		loaded = true;
		
		if(song != null) {
			song.playerLoaded();
		}
	}
	
	@XmlElement(name = "path")
	public String getXmlPath() {
		return path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public int getOffsetInMillis() {
		return offsetInMillis;
	}

	public void setOffsetInMillis(int offsetInMillis) {
		this.offsetInMillis = offsetInMillis;
	}

	@XmlTransient
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@XmlTransient
	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	@XmlTransient
	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
