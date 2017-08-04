package com.ascargon.rocketshow.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ShellManager {

	private Process process;

	public ShellManager() throws IOException {
		process = new ProcessBuilder("sh").start();

		// new Thread(() -> {
		// BufferedReader ir = new BufferedReader(new
		// InputStreamReader(process.getErrorStream()));
		// String line = null;
		// try {
		// while((line = ir.readLine()) != null){
		// System.out.printf(line);
		// }
		// } catch(IOException e) {}
		// }).start();
		//
		// new Thread(() -> {
		// BufferedReader ir = new BufferedReader(new
		// InputStreamReader(process.getInputStream()));
		// String line = null;
		// try {
		// while((line = ir.readLine()) != null){
		// System.out.printf("%s\n", line);
		// }
		// } catch(IOException e) {}
		// }).start();
		//
		// new Thread(() -> {
		// int exitCode = 0;
		// try {
		// exitCode = process.waitFor();
		// } catch(InterruptedException e) {
		// e.printStackTrace();
		// }
		// System.out.printf("Exited with code %d\n", exitCode);
		// }).start();
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

}
