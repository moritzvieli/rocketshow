package com.ascargon.rocketshow.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ascargon.rocketshow.Updater.UpdateState;
import com.ascargon.rocketshow.midi.MidiSignal;
import com.ascargon.rocketshow.song.Song.PlayState;

@XmlRootElement
public class State {

	private int currentSongIndex;
	private PlayState playState;
	private String currentSongName;
	private long currentSongDurationMillis;
	private Date lastStartTime;
	private MidiSignal midiSignal;
	private UpdateState updateState;
	private String currentSetListName;
	
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

	@XmlElement
	public String getCurrentSongName() {
		return currentSongName;
	}

	public void setCurrentSongName(String currentSongName) {
		this.currentSongName = currentSongName;
	}

	@XmlElement
	public long getCurrentSongDurationMillis() {
		return currentSongDurationMillis;
	}

	public void setCurrentSongDurationMillis(long currentSongDurationMillis) {
		this.currentSongDurationMillis = currentSongDurationMillis;
	}

	@XmlElement
	public Date getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	@XmlElement
	public MidiSignal getMidiSignal() {
		return midiSignal;
	}

	public void setMidiSignal(MidiSignal midiSignal) {
		this.midiSignal = midiSignal;
	}

	@XmlElement
	public UpdateState getUpdateState() {
		return updateState;
	}

	public void setUpdateState(UpdateState updateState) {
		this.updateState = updateState;
	}

	@XmlElement
	public String getCurrentSetListName() {
		return currentSetListName;
	}

	public void setCurrentSetListName(String currentSetListName) {
		this.currentSetListName = currentSetListName;
	}
	
}
