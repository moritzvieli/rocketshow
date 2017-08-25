package com.ascargon.rocketshow.dmx;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelMapping {

	private int channelFrom;
	private int channelTo;
	
	public int getChannelFrom() {
		return channelFrom;
	}
	public void setChannelFrom(int channelFrom) {
		this.channelFrom = channelFrom;
	}
	public int getChannelTo() {
		return channelTo;
	}
	public void setChannelTo(int channelTo) {
		this.channelTo = channelTo;
	}
	
}
