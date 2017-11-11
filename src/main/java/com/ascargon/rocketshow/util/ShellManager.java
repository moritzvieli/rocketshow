package com.ascargon.rocketshow.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class ShellManager {

	private Process process;

	public ShellManager() throws IOException {
		process = new ProcessBuilder("sh").start();
	}

	public void sendCommand(String command, boolean newLine) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		bufferedWriter.write(command);

		if (newLine) {
			bufferedWriter.newLine();
		}

		bufferedWriter.flush();
	}

	public void sendCommand(String command) throws IOException {
		sendCommand(command, true);
	}
	
	public InputStream getInputStream() {
		return process.getInputStream();
	}
	
	public void close() {
		if(process != null) {
			process.destroy();
		}
	}

}
