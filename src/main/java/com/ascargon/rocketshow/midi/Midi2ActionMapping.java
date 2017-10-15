package com.ascargon.rocketshow.midi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ascargon.rocketshow.dmx.Midi2DmxMapping;

@XmlRootElement
public class Midi2ActionMapping {

	private Midi2DmxMapping parent;

	private List<ActionMapping> actionMappingList = new ArrayList<ActionMapping>();

	// Completely ignore all parent's settings
	private Boolean overrideParent = false;

	public Midi2DmxMapping getParent() {
		return parent;
	}

	public void setParent(Midi2DmxMapping parent) {
		this.parent = parent;
	}

	@XmlElement
	public List<ActionMapping> getActionMappingList() {
		return actionMappingList;
	}

	public void setActionMappingList(List<ActionMapping> actionMappingList) {
		this.actionMappingList = actionMappingList;
	}

	@XmlElement
	public Boolean getOverrideParent() {
		return overrideParent;
	}

	public void setOverrideParent(Boolean overrideParent) {
		this.overrideParent = overrideParent;
	}

}
