package com.ascargon.rocketshow.video;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class VideoPlayer {

	private String path;
	private long position;
	private Process process;

	public void load(String path) {
		this.path = path;
	}

	public void setPositionInMillis(long position) {
		this.position = position;
	}

	private void sendCommand(String command, boolean newLine) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			bufferedWriter.write(command);
			
			if(newLine) {
				bufferedWriter.newLine();
			}
			
	        bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play() {
		try {
			process = new ProcessBuilder("sh").start();

//	        new Thread(() -> {
//	            BufferedReader ir = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//	            String line = null;
//	            try {
//	                while((line = ir.readLine()) != null){
//	                    System.out.printf(line);
//	                }
//	            } catch(IOException e) {}
//	        }).start();
//			
//	        new Thread(() -> {
//	            BufferedReader ir = new BufferedReader(new InputStreamReader(process.getInputStream()));
//	            String line = null;
//	            try {
//	                while((line = ir.readLine()) != null){
//	                    System.out.printf("%s\n", line);
//	                }
//	            } catch(IOException e) {}
//	        }).start();
//			
//	        new Thread(() -> {
//	            int exitCode = 0;
//	            try {
//	                exitCode = process.waitFor();
//	            } catch(InterruptedException e) {
//	                e.printStackTrace();
//	            }
//	            System.out.printf("Exited with code %d\n", exitCode);
//	        }).start();
	        
			String startPos = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(position),
					TimeUnit.MILLISECONDS.toMinutes(position)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(position)),
					TimeUnit.MILLISECONDS.toSeconds(position)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));

			// TODO to make it more exact: Set position one second before and delay the play by the remaining milliseconds
			
			sendCommand("omxplayer --pos " + startPos + " " + path, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void pause() {
		sendCommand("p", false);
	}

	public void resume() {
		sendCommand("p", false);
	}
	
	public void stop() {
		sendCommand("q", false);
	}
	
}
