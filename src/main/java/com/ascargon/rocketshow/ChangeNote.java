package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement
public class ChangeNote {

	final static Logger logger = Logger.getLogger(ChangeNote.class);

	private String version;
	private String changes;

	public ChangeNote() {
	}

	@XmlElement
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement
	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

}
