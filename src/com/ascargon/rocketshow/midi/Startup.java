package com.ascargon.rocketshow.midi;

import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import gnu.getopt.Getopt;

public class Startup {

    /**
     * Flag for debugging messages.
     * If true, some messages are dumped to the console
     * during operation.
     */
    private static boolean DEBUG = true;
	
    public void main(String[] args) throws Exception {

        // Display background image, when nothing else runs
        //Runtime.getRuntime().exec("sudo fbi -T 1 -a -noverbose /opt/rocketshow/img/test.jpg");


        //MidiFilePlayer[] playerList = new MidiFilePlayer[100];

        //for(int i = 0; i < 32; i ++) {
            //playerList[i] = new MidiFilePlayer();
            //playerList[i].play("/Users/moritzvieli/repo/rocketshow/test2.mid");
            //playerList[i].play("/opt/rocketshow/midi/test2.mid");
        //}

        // TODO Use this approach to kill the video if neccessary
        // https://stackoverflow.com/questions/15095819/how-to-kill-runtime-exec
        // Play video
        //Runtime.getRuntime().exec("omxplayer /opt/rocketshow/video/test.mp4").waitFor();


        // TODO
        //Thread.sleep(5000000);

		/*
         *	The device name/index to listen to.
		 */
        String strDeviceName = null;
        int nDeviceIndex = -1;
        boolean bUseDefaultSynthesizer = false;
        // TODO: synchronize options with MidiPlayer


		/*
         *	Parsing of command-line options takes place...
		 */
        Getopt g = new Getopt("MidiInDump", args, "hlsd:n:D");
        int c;
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'h':
                    printUsageAndExit();

                case 'l':
                    MidiCommon.listDevicesAndExit(true, false);

                case 's':
                    bUseDefaultSynthesizer = true;
                    break;

                case 'd':
                    strDeviceName = g.getOptarg();
                    if (DEBUG) {
                        out("MidiInDump.main(): device name: " + strDeviceName);
                    }
                    break;

                case 'n':
                    nDeviceIndex = Integer.parseInt(g.getOptarg());
                    if (DEBUG) {
                        out("MidiInDump.main(): device index: " + nDeviceIndex);
                    }
                    break;

                case 'D':
                    DEBUG = true;
                    break;

                case '?':
                    printUsageAndExit();

                default:
                    out("MidiInDump.main(): getopt() returned " + c);
                    break;
            }
        }

        if ((strDeviceName == null) && (nDeviceIndex < 0)) {
            out("device name/index not specified!");
            printUsageAndExit();
        }

        MidiDevice.Info info;
        if (strDeviceName != null) {
            info = MidiCommon.getMidiDeviceInfo(strDeviceName, false);
        } else {
            info = MidiCommon.getMidiDeviceInfo(nDeviceIndex);
        }
        if (info == null) {
            if (strDeviceName != null) {
                out("no device info found for name " + strDeviceName);
            } else {
                out("no device info found for index " + nDeviceIndex);
            }
            //System.exit(1);
        }
//        MidiDevice inputDevice = null;
//        try {
//            inputDevice = MidiSystem.getMidiDevice(info);
//            inputDevice.open();
//        } catch (MidiUnavailableException e) {
//            out(e);
//        }
//        if (inputDevice == null) {
//            out("wasn't able to retrieve MidiDevice");
//            System.exit(1);
//        }
//        Receiver r = new DumpReceiver(System.out);
//        try {
//            Transmitter t = inputDevice.getTransmitter();
//            t.setReceiver(r);
//        } catch (MidiUnavailableException e) {
//            out("wasn't able to connect the device's Transmitter to the Receiver:");
//            out(e);
//            inputDevice.close();
//            System.exit(1);
//        }
//        if (bUseDefaultSynthesizer) {
//            Synthesizer synth = MidiSystem.getSynthesizer();
//            synth.open();
//            r = synth.getReceiver();
//            try {
//                Transmitter t = inputDevice.getTransmitter();
//                t.setReceiver(r);
//            } catch (MidiUnavailableException e) {
//                out("wasn't able to connect the device's Transmitter to the default Synthesizer:");
//                out(e);
//                inputDevice.close();
//                System.exit(1);
//            }
//        }
//        out("now running; interupt the program with [ENTER] when finished");
//
//        try {
//            System.in.read();
//        } catch (IOException ioe) {
//        }
//        inputDevice.close();
//        out("Received " + ((DumpReceiver) r).seCount + " sysex messages with a total of " + ((DumpReceiver) r).seByteCount + " bytes");
//        out("Received " + ((DumpReceiver) r).smCount + " short messages with a total of " + ((DumpReceiver) r).smByteCount + " bytes");
//        out("Received a total of " + (((DumpReceiver) r).smByteCount + ((DumpReceiver) r).seByteCount) + " bytes");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            if (DEBUG) {
//                out(e);
//            }
//        }
    }


    private static void printUsageAndExit() {
        out("MidiInDump: usage:");
        out("  java MidiInDump -h");
        out("    gives help information");
        out("  java MidiInDump -l");
        out("    lists available MIDI devices");
        out("  java MidiInDump [-D] [-d <input device name>] [-n <device index>]");
        out("    -d <input device name>\treads from named device (see '-l')");
        out("    -n <input device index>\treads from device with given index(see '-l')");
        out("    -D\tenables debugging output");
        //System.exit(1);
    }


    private static void out(String strMessage) {
        System.out.println(strMessage);
    }


    private static void out(Throwable t) {
        if (DEBUG) {
            t.printStackTrace();
        } else {
            out(t.toString());
        }
    }
	
}
