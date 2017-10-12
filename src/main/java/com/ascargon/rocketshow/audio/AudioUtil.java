package com.ascargon.rocketshow.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

public class AudioUtil {

	final static Logger logger = Logger.getLogger(AudioUtil.class);
	
	private List<Mixer.Info> filterDevices(final Line.Info supportedLine) {
	    List<Mixer.Info> result = new ArrayList<Mixer.Info>();

	    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
	    
	    for (Mixer.Info info : mixerInfo) {
	        Mixer mixer = AudioSystem.getMixer(info);
	        
	        if (mixer.isLineSupported(supportedLine)) {
	            result.add(info);
	        }
	    }
	    
	    return result;
	}
	
	public List<Mixer.Info> getOutputAudioDevices() {
		return filterDevices(new Line.Info(TargetDataLine.class));
	}

	public List<Mixer.Info> getInputAudioDevices() {
		return filterDevices(new Line.Info(SourceDataLine.class));
	}
	
}
