package com.ascargon.rocketshow.dmx;

import java.util.HashMap;

public class Midi2DmxMapping {

	public enum MappingType {
		SIMPLE, // The MIDI values 0-126 are mapped to a DMX channel and the
				// value
				// is composed by the velocity multiplied by 2
		EXACT // MIDI channels 0-16 are mapped to a DMX channel and the value is
				// composed by adding the value and the velocity
	}

	private MappingType mappingType = MappingType.SIMPLE;

	private Midi2DmxMapping parent;

	private HashMap<Integer, Integer> channelMap;
	
	private Integer channelOffset;

	// Completely ignore all parent's settings
	private boolean overrideParent = false;

	public Midi2DmxMapping getParent() {
		return parent;
	}

	public void setParent(Midi2DmxMapping parent) {
		this.parent = parent;
	}

	public boolean isOverrideParent() {
		return overrideParent;
	}

	public void setOverrideParent(boolean overrideParent) {
		this.overrideParent = overrideParent;
	}

	public MappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(MappingType mappingType) {
		this.mappingType = mappingType;
	}

	public HashMap<Integer, Integer> getChannelMap() {
		return channelMap;
	}

	public void setChannelMap(HashMap<Integer, Integer> channelMap) {
		this.channelMap = channelMap;
	}

	public Integer getChannelOffset() {
		return channelOffset;
	}

	public void setChannelOffset(Integer channelOffset) {
		this.channelOffset = channelOffset;
	}

}
