package com.ascargon.rocketshow.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

public class MidiFilePlayer implements Receiver {

    public void play(String fileName) throws Exception {
        Sequencer sequencer = MidiSystem.getSequencer(false);
        sequencer.open();

        InputStream is = new BufferedInputStream(new FileInputStream(new File(fileName)));

        sequencer.setSequence(is);

        sequencer.getTransmitter().setReceiver(this);

        sequencer.start();
    }

    public void send(MidiMessage message, long timeStamp) {
    		ShortMessage a = new ShortMessage();
    	
        if(message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            int channel = sm.getChannel();

            if (sm.getCommand() == ShortMessage.NOTE_ON) {
                int key = sm.getData1();
                int velocity = sm.getData2();
                Note note = new Note(key);
                System.out.println("Channel " + channel + " note on " + note);
            } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                int key = sm.getData1();
                int velocity = sm.getData2();
                Note note = new Note(key);
                System.out.println("Channel " + channel + " note off " + note);
            } else {
                System.out.println("Command:" + sm.getCommand());
            }
        }
    }

    public void close() {}

}
