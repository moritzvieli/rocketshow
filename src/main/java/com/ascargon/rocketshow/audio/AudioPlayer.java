package com.ascargon.rocketshow.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

import org.apache.log4j.Logger;

public class AudioPlayer {

	final static Logger logger = Logger.getLogger(AudioPlayer.class);

	public void load() throws IOException {

	}

	public void setPositionInMillis(long position) {
		// TODO
	}

	private void lineClose(int soundPort) throws LineUnavailableException {
		// Port.Info lineInfo = outputPorts.get(soundPort);
		// Line line = (Port) AudioSystem.getLine(lineInfo);
		// line.close();
	}

	public void play(File file) throws Exception {
		String path = file.getPath();
//		InputStream is = new FileInputStream(path);
//		BufferedInputStream bis = new BufferedInputStream(is);
//		AudioInputStream inputStream = AudioSystem.getAudioInputStream(bis);
//		AudioFormat format = inputStream.getFormat();
//
//		logger.info("Start playing audio file '" + path + "'");
//		
//		Mixer.Info mi = AudioSystem.getMixerInfo()[0];
//
//		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getSourceDataLine(format, mi);
//		sourceDataLine.open(format);
//		sourceDataLine.start();
//
//		byte[] buf = new byte[1024];
//		int bytesRead;
//		
//		while ((bytesRead = inputStream.read(buf)) != -1) {
//			sourceDataLine.write(buf, 0, bytesRead);
//		}
//		
//		logger.info("Finished playing audio file + '" + path + "'");
//		
//		inputStream.close();
//
//		sourceDataLine.drain();
//		sourceDataLine.stop();
//		sourceDataLine.close();

		// lineClose(soundPort);
		
        try {
	        	//read audio data from whatever source (file/classloader/etc.)
        		logger.info("Start playing audio file '" + path + "'");
        	
        		//audioSrc = Sound.class.getClassLoader().getResourceAsStream(path + "select.wav");
        		
	        	InputStream audioSrc = AudioPlayer.class.getResourceAsStream(path);
	        	
	        	logger.info("audioSrc: " + audioSrc);
	        	
	        	//add buffer for mark/reset support
	        	InputStream bufferedIn = new BufferedInputStream(audioSrc);
	        	
	        	logger.info("bufferedIn: " + bufferedIn);

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
 
            AudioFormat format = audioStream.getFormat();
            
            AudioLine audioLine = new AudioLine();
            audioLine.setId(10);
            
            Line audioClip = AudioUtil.getHardwareLine(audioLine);
            logger.info("INFO: " + audioClip);
            logger.info("INFO: " + audioClip.getClass());
            
            //DataLine.Info info = new DataLine.Info(Clip.class, format);

            //Clip audioClip = (Clip) AudioSystem.getLine(info);
            //Clip audioClip = (Clip) l;
 
            //audioClip.addLineListener(this);
 
//            audioClip.open(audioStream);
//
//            audioClip.start();
             
//            while (!playCompleted) {
//                // wait for the playback completes
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
             
            //audioClip.close();
             
        } catch (Exception e) {
            logger.error("Could not play", e);
        }
	}

	public void close() {
		// TODO
	}

	public void pause() throws IOException {
		// TODO
	}

	public void resume() throws IOException {
		// TODO
	}

	public void stop() throws IOException {
		// TODO
	}

}
