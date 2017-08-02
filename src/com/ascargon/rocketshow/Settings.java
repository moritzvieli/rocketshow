package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {

	private String defaultImagePath;

	@XmlElement
	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}
	
}
