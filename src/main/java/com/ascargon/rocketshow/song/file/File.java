package com.ascargon.rocketshow.song.file;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.Song;

@XmlRootElement
abstract public class File implements PlayerLoadedListener {

	public final static String MEDIA_PATH = "media/";
	
	private String name;
	
	private Manager manager;
	private Song song;
	
	private boolean loading = false;
	private boolean loaded = false;
	
	private boolean active = true;

	private long durationMillis;
	
	// Play offset
	private int offsetMillis = 0;
	
	abstract public void load() throws Exception;
	
	abstract public void close() throws Exception;
	
	abstract public void play() throws Exception;
	
	abstract public void pause() throws Exception;
	
	abstract public void resume() throws Exception;
	
	abstract public void stop() throws Exception;
	
	@Override
	public void playerLoaded() {
		loaded = true;
		loading = false;
		
		if(song != null) {
			song.playerLoaded();
		}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public int getOffsetMillis() {
		return offsetMillis;
	}

	public void setOffsetMillis(int offsetMillis) {
		this.offsetMillis = offsetMillis;
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

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

}
