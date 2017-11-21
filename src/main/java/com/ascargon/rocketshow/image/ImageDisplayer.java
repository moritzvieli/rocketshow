package com.ascargon.rocketshow.image;

import java.io.IOException;

import com.ascargon.rocketshow.util.ShellManager;

public class ImageDisplayer {

	private ShellManager shellManager;
	
	public ImageDisplayer() throws IOException {
		shellManager = new ShellManager(new String[] { "sh" });
	}
	
	public void display(String path) throws IOException {
		shellManager.sendCommand("sudo fbi -T 1 -a -noverbose " + path, true);
	}
	
}
