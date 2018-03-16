package com.ascargon.rocketshow.util;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement
public class VersionInfo {

	final static Logger logger = Logger.getLogger(VersionInfo.class);

	private String version;
	private Date date;
	private List<ChangeNote> changeNotes;

	public VersionInfo() {
	}

	@XmlElement
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@XmlElement(name = "changeNote")
	@XmlElementWrapper(name = "changeNoteList")
	public List<ChangeNote> getChangeNotes() {
		return changeNotes;
	}

	public void setChangeNotes(List<ChangeNote> changeNotes) {
		this.changeNotes = changeNotes;
	}

}
