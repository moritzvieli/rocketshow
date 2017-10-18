package com.ascargon.rocketshow.midi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Map one specific MIDI event to an action.
 *
 * @author Moritz Vieli
 */
@XmlRootElement
public class Midi2ActionMapping {

	private List<ActionMapping> actionMappingList = new ArrayList<ActionMapping>();

	// Completely ignore all parent's settings
	private Boolean overrideParent = false;

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
