package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MidiActionMapping {

	public enum MidiAction {
		PLAY, TOGGLE_PLAY, PAUSE, RESUME, NEXT_SONG, PREVIOUS_SONG, STOP
	}

	// TODO Input channel (or all channels), input note -> action-list from enum

}
