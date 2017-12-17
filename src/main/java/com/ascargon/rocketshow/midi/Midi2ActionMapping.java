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
public class Midi2ActionMapping {

	private List<ActionMapping> actionMappingList = new ArrayList<ActionMapping>();

	@XmlElement(name = "actionMapping")
	@XmlElementWrapper(name = "actionMappingList")
	public List<ActionMapping> getActionMappingList() {
		return actionMappingList;
	}

	public void setActionMappingList(List<ActionMapping> actionMappingList) {
		this.actionMappingList = actionMappingList;
	}

}
