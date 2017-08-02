package com.ascargon.rocketshow.image;

import com.ascargon.rocketshow.util.ShellManager;

public class ImageDisplayer {

	private ShellManager shellManager;
	
	public ImageDisplayer() {
		shellManager = new ShellManager();
	}
	
	public void display(String path) {
		shellManager.sendCommand("sudo fbi -T 1 -a -noverbose " + path);
	}
	
}
