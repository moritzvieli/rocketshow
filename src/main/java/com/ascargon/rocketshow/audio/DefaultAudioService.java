package com.ascargon.rocketshow.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ascargon.rocketshow.Settings;
import com.ascargon.rocketshow.composition.CompositionPlayer;
import com.ascargon.rocketshow.gstreamer.GstApi;
import org.freedesktop.gstreamer.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DefaultAudioService implements AudioService {

    private final static Logger logger = LoggerFactory.getLogger(DefaultAudioService.class);

    private AudioDevice getAudioDeviceFromString(String line) {
        AudioDevice audioDevice = new AudioDevice();

        audioDevice.setId(Integer.parseInt(line.substring(0, 3).trim()));
        audioDevice.setKey(line.substring(4, 19).trim());
        audioDevice.setName(line.substring(21).trim());

        return audioDevice;
    }

    @Override
    public List<AudioDevice> getAudioDevices() {
        List<AudioDevice> audioDeviceList = new ArrayList<>();

        logger.debug("List audio devices...");

        try {
            Process process = new ProcessBuilder("cat", "/proc/asound/cards").start();

            Thread readerThread = new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                boolean readLine = true;

                try {
                    while ((line = reader.readLine()) != null) {
                        logger.trace("Output from audio device list process: " + line);

                        // Only read the uneven lines. The even ones contain
                        // unneccessary information.
                        if (readLine) {
                            readLine = false;

                            AudioDevice audioDevice = getAudioDeviceFromString(line);

                            if (audioDevice.getName() != null && audioDevice.getName().length() > 0
                                    && !audioDevice.getKey().equals("ALSA")) {

                                audioDeviceList.add(audioDevice);
                            }
                        } else {
                            readLine = true;
                        }
                    }
                } catch (IOException e) {
                    logger.error("Could not read audio device list output", e);
                }
            });

            readerThread.start();

            try {
                readerThread.join();
            } catch (InterruptedException e) {
                logger.error("Could not wait for the list of audio devices", e);
            }
        } catch (IOException e) {
            logger.error("Could not list the audio devices", e);
        }

        logger.debug("Audio devices listed");

        return audioDeviceList;
    }

    @Override
    public boolean isAudioChannelCountCompatible(Settings settings, int channelCount) {
        // Build a test Gstreamer pipeline and query the channel count from the sink
//        final Pipeline pipeline = new Pipeline();
//
//        Element audioTestSrc = ElementFactory.make("audiotestsrc", "audiotestsrc");
//        audioTestSrc.set("volume", 0d);
//        pipeline.add(audioTestSrc);
//
//        Element audioConvert = ElementFactory.make("audioconvert", "audioconvert");
//        pipeline.add(audioConvert);
//
//        final Element osxAudioSink = ElementFactory.make("osxaudiosink", "osxaudiosink");
//        pipeline.add(osxAudioSink);
//
//        audioTestSrc.link(audioConvert);
//        audioConvert.link(osxAudioSink);
//
//        Bus bus = GstApi.GST_API.gst_element_get_bus(pipeline);
//        bus.connect((GstObject source, State old, State newState, State pending) -> {
//            if (source.getTypeName().equals("GstPipeline")) {
//                if (newState == State.PLAYING) {
//                    logger.info("Channels: " + osxAudioSink.getSinkPads().get(0).getNegotiatedCaps().getStructure(0).getInteger("channels"));
//
//                    pipeline.stop();
//                    pipeline.dispose();
//                }
//            }
//        });
//        GstApi.GST_API.gst_object_unref(bus);
//
//        pipeline.play();

        return true;
    }

}
