package com.ascargon.rocketshow.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ShellManager {

	private Process process;
	
	public ShellManager() {
		try {
			process = new ProcessBuilder("sh").start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
//        new Thread(() -> {
//        BufferedReader ir = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//        String line = null;
//        try {
//            while((line = ir.readLine()) != null){
//                System.out.printf(line);
//            }
//        } catch(IOException e) {}
//    }).start();
//	
//    new Thread(() -> {
//        BufferedReader ir = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line = null;
//        try {
//            while((line = ir.readLine()) != null){
//                System.out.printf("%s\n", line);
//            }
//        } catch(IOException e) {}
//    }).start();
//	
//    new Thread(() -> {
//        int exitCode = 0;
//        try {
//            exitCode = process.waitFor();
//        } catch(InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.printf("Exited with code %d\n", exitCode);
//    }).start();
	}
	
	public void sendCommand(String command, boolean newLine) {
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
	
	public void sendCommand(String command) {
		sendCommand(command, true);
	}
	
}
