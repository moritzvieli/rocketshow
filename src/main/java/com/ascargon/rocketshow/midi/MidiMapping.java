package com.ascargon.rocketshow.midi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MidiMapping {

	private MidiMapping parent;

	private List<ChannelMapping> channelMap = new ArrayList<ChannelMapping>();
	
	private Integer channelOffset;
	private Integer noteOffset;

	// Completely ignore all parent's settings
	private Boolean overrideParent = false;

	public MidiMapping getParent() {
		return parent;
	}

	public void setParent(MidiMapping parent) {
		this.parent = parent;
	}

	@XmlElement
	public Boolean isOverrideParent() {
		return overrideParent;
	}

	public void setOverrideParent(Boolean overrideParent) {
		this.overrideParent = overrideParent;
	}

	@XmlElement
	public List<ChannelMapping> getChannelMap() {
		return channelMap;
	}

	public void setChannelMap(List<ChannelMapping> channelMap) {
		this.channelMap = channelMap;
	}

	@XmlElement
	public Integer getChannelOffset() {
		return channelOffset;
	}

	public void setChannelOffset(Integer channelOffset) {
		this.channelOffset = channelOffset;
	}

	@XmlElement
	public Integer getNoteOffset() {
		return noteOffset;
	}

	public void setNoteOffset(Integer noteOffset) {
		this.noteOffset = noteOffset;
	}

}
