package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {

	private String currentSetListPath;
	
	private Integer currentSongIndex;

	@XmlElement
	public String getCurrentSetListPath() {
		return currentSetListPath;
	}

	public void setCurrentSetListPath(String currentSetListPath) {
		this.currentSetListPath = currentSetListPath;
	}

	@XmlElement
	public Integer getCurrentSongIndex() {
		return currentSongIndex;
	}

	public void setCurrentSongIndex(Integer currentSongIndex) {
		this.currentSongIndex = currentSongIndex;
	}
	
}
