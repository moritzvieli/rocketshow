package com.ascargon.showmachine.MidiFilePlayer;

import javax.sound.midi.*;
import java.io.*;

/**
 * Created by moritzvieli on 21.07.17.
 */
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
