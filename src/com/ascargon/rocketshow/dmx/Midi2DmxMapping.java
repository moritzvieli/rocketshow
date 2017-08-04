package com.ascargon.rocketshow.dmx;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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

	private List<ChannelMapping> channelMap;
	
	private Integer channelOffset;

	// Completely ignore all parent's settings
	private boolean overrideParent = false;

	public Midi2DmxMapping getParent() {
		return parent;
	}

	public void setParent(Midi2DmxMapping parent) {
		this.parent = parent;
	}

	@XmlElement
	public boolean isOverrideParent() {
		return overrideParent;
	}

	public void setOverrideParent(boolean overrideParent) {
		this.overrideParent = overrideParent;
	}

	@XmlElement
	public MappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(MappingType mappingType) {
		this.mappingType = mappingType;
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

}
