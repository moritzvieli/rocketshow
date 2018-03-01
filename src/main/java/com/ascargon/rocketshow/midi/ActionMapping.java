package com.ascargon.rocketshow.midi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Map one specific MIDI event to an action.
 *
 * @author Moritz Vieli
 */
@XmlRootElement
public class ActionMapping {

	public enum MidiAction {
		PLAY, TOGGLE_PLAY, PAUSE, RESUME, NEXT_SONG, PREVIOUS_SONG, STOP, SET_SONG_INDEX, REBOOT
	}

	private MidiAction action;

	// If null -> all channels
	private Integer channelFrom;

	// The note, this action should be triggered. If null -> all notes
	private Integer noteFrom;

	// Should this action apply to a remote device?
	private List<Integer> remoteDeviceIds = new ArrayList<Integer>();

	// Should this action apply locally?
	private boolean executeLocally = true;

	@XmlElement
	public MidiAction getAction() {
		return action;
	}

	public void setAction(MidiAction action) {
		this.action = action;
	}

	@XmlElement
	public Integer getChannelFrom() {
		return channelFrom;
	}

	public void setChannelFrom(Integer channelFrom) {
		this.channelFrom = channelFrom;
	}

	@XmlElement
	public Integer getNoteFrom() {
		return noteFrom;
	}

	public void setNoteFrom(Integer noteFrom) {
		this.noteFrom = noteFrom;
	}

	@XmlElement
	public boolean isExecuteLocally() {
		return executeLocally;
	}

	public void setExecuteLocally(boolean executeLocally) {
		this.executeLocally = executeLocally;
	}

	@XmlElement(name = "remoteDevice")
	@XmlElementWrapper(name = "remoteDeviceList")
	public List<Integer> getRemoteDeviceIds() {
		return remoteDeviceIds;
	}

	public void setRemoteDeviceIds(List<Integer> remoteDeviceIds) {
		this.remoteDeviceIds = remoteDeviceIds;
	}

}
