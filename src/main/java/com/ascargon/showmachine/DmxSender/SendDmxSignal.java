package com.ascargon.showmachine.DmxSender;

import com.juanjo.openDmx.OpenDmx;

import java.io.IOException;

/**
 * Created by moritzvieli on 21.07.17.
 */
public class SendDmxSignal {

    public static final int MAX_CHANNELS = 512;

    public SendDmxSignal() {
        //open send mode
        if(!OpenDmx.connect(OpenDmx.OPENDMX_TX)){
            System.out.println("Open Dmx widget not detected!");
            return;
        }

        // Does it work without close (e.g. start java again without restarting
        // device?
        //close
        //OpenDmx.disconnect();
    }

    public void send(int channel, int value) {
        OpenDmx.setValue(channel,value);
    }

}
