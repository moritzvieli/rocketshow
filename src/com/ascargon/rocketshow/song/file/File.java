package com.ascargon.rocketshow.song.file;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ascargon.rocketshow.Manager;

@XmlRootElement
abstract public class File {

	private String path;
	
	private Manager manager;

	// Play offset
	private int offsetInMillis = 0;
	
	abstract public void load();
	
	abstract public void play();
	
	abstract public void pause();
	
	abstract public void resume();
	
	@XmlElement(name = "path")
	public String getXmlPath() {
		return path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public int getOffsetInMillis() {
		return offsetInMillis;
	}

	public void setOffsetInMillis(int offsetInMillis) {
		this.offsetInMillis = offsetInMillis;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
