package com.ascargon.rocketshow.audio;

import com.ascargon.rocketshow.composition.CompositionFile;

import javax.xml.bind.annotation.XmlElement;

public class AudioCompositionFile extends CompositionFile {

    private String outputBus;
    private int channels = 2;
    private float audioVolume = 1;

    public String getOutputBus() {
        return outputBus;
    }

    public void setOutputBus(String outputBus) {
        this.outputBus = outputBus;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public float getAudioVolume() {
        return audioVolume;
    }

    public void setAudioVolume(float audioVolume) {
        this.audioVolume = audioVolume;
    }

    public CompositionFileType getType() {
        return CompositionFileType.AUDIO;
    }

}
