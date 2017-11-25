package com.ascargon.rocketshow.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ascargon.rocketshow.song.Song.PlayState;

@XmlRootElement
public class State {

	private int currentSongIndex;
	private PlayState playState;

	@XmlElement
	public int getCurrentSongIndex() {
		return currentSongIndex;
	}

	public void setCurrentSongIndex(int currentSongIndex) {
		this.currentSongIndex = currentSongIndex;
	}

	@XmlElement
	public PlayState getPlayState() {
		return playState;
	}

	public void setPlayState(PlayState playState) {
		this.playState = playState;
	}
	
}
