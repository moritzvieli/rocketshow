package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {

	private String currentSetListName;
	
	private Integer currentSongIndex;

	@XmlElement
	public String getCurrentSetListName() {
		return currentSetListName;
	}

	public void setCurrentSetListName(String currentSetListName) {
		this.currentSetListName = currentSetListName;
	}

	@XmlElement
	public Integer getCurrentSongIndex() {
		return currentSongIndex;
	}

	public void setCurrentSongIndex(Integer currentSongIndex) {
		this.currentSongIndex = currentSongIndex;
	}
	
}
