package com.ascargon.rocketshow.song.file;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ascargon.rocketshow.Manager;

@XmlRootElement
abstract public class File {

	private String path;
	
	private Manager manager;

	// Play offset
	private int offsetInMillis = 0;
	
	abstract public void load() throws Exception;
	
	abstract public void play() throws Exception;
	
	abstract public void pause() throws Exception;
	
	abstract public void resume() throws Exception;
	
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

	@XmlTransient
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
