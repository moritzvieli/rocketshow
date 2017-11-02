package com.ascargon.rocketshow.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;
import org.jaudiolibs.jnajack.Jack;
import org.jaudiolibs.jnajack.JackClient;
import org.jaudiolibs.jnajack.JackException;
import org.jaudiolibs.jnajack.JackOptions;
import org.jaudiolibs.jnajack.JackPort;
import org.jaudiolibs.jnajack.JackPortFlags;
import org.jaudiolibs.jnajack.JackPortType;
import org.jaudiolibs.jnajack.JackProcessCallback;
import org.jaudiolibs.jnajack.JackStatus;

public class AudioUtil {

	final static Logger logger = Logger.getLogger(AudioUtil.class);

	private JackClient client;
	private Jack jack;

	private JackPort[] outputPorts;

	private FloatBuffer outputBuffer;

	private Callback callback;

	AudioInputStream is;

	public AudioUtil() {
		logger.info("AAAAA");

		File file = new File("/opt/rocketshow/test.wav");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file.getPath());
			
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			try {
				is = AudioSystem.getAudioInputStream(bis);
			} catch (Exception e) {
				logger.error("TODO", e);
			}
		} catch (FileNotFoundException e) {
			logger.error("TODO", e);
		}

		// BufferedInputStream bis = new BufferedInputStream(is);
		// AudioInputStream inputStream = AudioSystem.getAudioInputStream(bis);

		EnumSet<JackStatus> status = EnumSet.noneOf(JackStatus.class);

		this.callback = new Callback();

		try {
			jack = Jack.getInstance();
			logger.info("BBB");

			EnumSet<JackOptions> options = EnumSet.of(JackOptions.JackNoStartServer);

			client = jack.openClient("default", options, status);

			logger.info("Successfully initialized Jack");

			EnumSet<JackPortFlags> flags = EnumSet.of(JackPortFlags.JackPortIsOutput);

			String[] names = jack.getPorts(client, "", JackPortType.AUDIO, flags);

			outputPorts = new JackPort[names.length];

			for (int i = 0; i < names.length; i++) {
				logger.info("GOT PORT: " + names[i]);

				outputPorts[i] = client.registerPort(names[i], JackPortType.AUDIO, flags);
			}

			client.setProcessCallback(this.callback);

			client.activate();

		} catch (JackException e) {
			logger.error("TODO", e);
		}

		for (JackStatus s : status) {
			logger.info("Status: " + s.toString());
		}
	}

	// private void processBuffers(int nframes) {
	// for (int i = 0; i < inputPorts.length; i++) {
	// inputBuffers[i] = inputPorts[i].getFloatBuffer();
	// }
	// for (int i = 0; i < outputPorts.length; i++) {
	// outputBuffers[i] = outputPorts[i].getFloatBuffer();
	// }
	// processor.process(channelNumber, inputBuffers, outputBuffers);
	// }

	private class Callback implements JackProcessCallback {

		public boolean process(JackClient client, final int nframes) {

//            if (!active) {
//                return false;
//            } else {
                try {
                    //processBuffers(nframes);
                		byte[] buf = new byte[1024];
                		is.read(buf);
                		
                		is.
                		outputPorts[0].getFloatBuffer().put(is);
                		
                    
                    return true;
                } catch (Exception ex) {
                    System.out.println("ERROR : " + ex);
//                    active = false;
                    return false;
                }

//            }
        }
	}

	public static Line getHardwareLine(AudioLine audioLine) {
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

		int index = 0;

		for (Mixer.Info info : mixerInfo) {
			Mixer mixer = AudioSystem.getMixer(info);

			Line.Info[] lines = mixer.getSourceLineInfo();

			for (int i = 0; i < lines.length; i++) {
				Line.Info line = lines[i];

				if (line.getLineClass() == javax.sound.sampled.Port.class) {
					Port.Info portInfo = (Port.Info) line;

					if (index == audioLine.getId()) {
						try {
							return mixer.getLine((Line.Info) portInfo);
						} catch (LineUnavailableException e) {
							logger.error("TODO", e);
						}
					}
				}

				index++;
			}
		}

		return null;
	}

	private List<AudioLine> filterLine(final Line.Info supportedLine) {
		List<AudioLine> result = new ArrayList<AudioLine>();

		// Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		//
		// int index = 0;
		//
		// for (Mixer.Info info : mixerInfo) {
		// Mixer mixer = AudioSystem.getMixer(info);
		//
		// // //if (mixer.isLineSupported(supportedLine)) {
		// // Line.Info[] lines = mixer.getTargetLineInfo();
		// //
		// // for (int i = 0; i < lines.length; i++) {
		// // Line.Info line = lines[i];
		// //
		// // if(line.) {
		// //
		// //
		// // AudioLine resultLine = new AudioLine();
		// //
		// // resultLine.setId(i);
		// // resultLine.setVendor(info.getVendor());
		// // resultLine.setName(info.getName());
		// // resultLine.setDescription(info.getDescription());
		// //
		// // result.add(resultLine);
		// // }
		// // }
		// // //}
		//
		// Line.Info[] lines = mixer.getSourceLineInfo();
		//
		// for (int i = 0; i < lines.length; i++) {
		// Line.Info line = lines[i];
		//
		// // AudioLine resultLine = new AudioLine();
		//
		// // logger.debug("Found target line: " + li + " " +
		// // li.getClass());
		// // outputLines.add(li);
		//
		// // if (line.getLineClass() == javax.sound.sampled.Clip.class) {
		// // Port.Info portInfo = (Port.Info) line;
		// logger.info("Class: " + line.getLineClass());
		// AudioLine resultLine = new AudioLine();
		//
		// resultLine.setId(index);
		// resultLine.setVendor(info.getVendor());
		// // resultLine.setName(portInfo.getName());
		// resultLine.setDescription(info.getDescription());
		//
		// result.add(resultLine);
		//
		// index++;
		//
		// // Port.Info portInfo = (Port.Info) li;
		// // logger.debug("port found " + portInfo.getName() + " is
		// // source " + portInfo.isSource());
		// // outputPorts.add(portInfo);
		// // }
		// }
		// }

		String[] physical;
		try {
			logger.info("Jack: " + jack);

			physical = jack.getPorts(client, null, JackPortType.AUDIO,
					EnumSet.of(JackPortFlags.JackPortIsInput, JackPortFlags.JackPortIsPhysical));

			for (int i = 0; i < physical.length; i++) {
				logger.debug("output port " + physical[i]);
			}
		} catch (JackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public List<AudioLine> getOutputAudioLines() {
		return filterLine(new Line.Info(SourceDataLine.class));
	}

	public List<AudioLine> getInputAudioLines() {
		return filterLine(new Line.Info(TargetDataLine.class));
	}

}
