package com.ascargon.rocketshow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines a remote RocketShow device to be triggered by this one.
 *
 * @author Moritz A. Vieli
 */
@XmlRootElement
public class RemoteDevice {

	// The id of the remote device
	private int id;
	
	// The name of the remote device
	private String name;

	// The host address (IP or hostname) of the remote device
	private String host;

	@XmlElement
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
