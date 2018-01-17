package com.ascargon.rocketshow.song;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetListSong {

	private String name;
	private long durationMillis;
	private boolean autoStartNextSong = false;
	
	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	@XmlElement
	public boolean isAutoStartNextSong() {
		return autoStartNextSong;
	}

	public void setAutoStartNextSong(boolean autoStartNextSong) {
		this.autoStartNextSong = autoStartNextSong;
	}

}
