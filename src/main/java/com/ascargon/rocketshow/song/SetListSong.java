package com.ascargon.rocketshow.song;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetListSong {

	private String name;

	public String getName() {
		return name;
	}

	public void setPath(String name) {
		this.name = name;
	}
	
}
