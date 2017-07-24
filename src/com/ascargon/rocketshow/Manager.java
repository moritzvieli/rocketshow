package com.ascargon.rocketshow;

import com.ascargon.rocketshow.dmx.SendDmxSignal;

public class Manager {

	private SendDmxSignal sendDmxSignal;
	
	public void init() {
		sendDmxSignal = new SendDmxSignal();
	}
	
	public String test() {
		return "Hello there";
	}
	
}
