package com.ascargon.rocketshow.song;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetListSong {

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
