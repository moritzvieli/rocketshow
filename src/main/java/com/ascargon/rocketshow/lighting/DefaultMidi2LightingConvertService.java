package com.ascargon.rocketshow.lighting;

import com.ascargon.rocketshow.lighting.Midi2LightingMapping.MappingType;
import org.springframework.stereotype.Service;

import javax.sound.midi.ShortMessage;

@Service
public class DefaultMidi2LightingConvertService implements Midi2LightingConvertService {

    private final LightingService lightingService;

    public DefaultMidi2LightingConvertService(LightingService lightingService) {
        this.lightingService = lightingService;
    }

    private void mapSimple(ShortMessage shortMessage, LightingUniverse lightingUniverse) {
        if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
            int valueTo = shortMessage.getData2() * 2;

            // Extend the last note to the max
            // TODO enable this feature by a mapping-setting
            if (valueTo == 254) {
                valueTo = 255;
            }

            lightingUniverse.getUniverse().put(shortMessage.getData1(), valueTo);
            lightingService.send();
        } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
            int valueTo = 0;

            lightingUniverse.getUniverse().put(shortMessage.getData1(), valueTo);
            lightingService.send();
        }
    }

    private void mapExact() {
        // TODO
    }

    @Override
    public void processMidiEvent(ShortMessage shortMessage, Midi2LightingMapping midi2LightingMapping, LightingUniverse lightingUniverse) {
        // Map the MIDI event and send the appropriate lighting signal

        // Only react to NOTE_ON/NOTE_OFF events
        if (shortMessage.getCommand() != ShortMessage.NOTE_ON && shortMessage.getCommand() != ShortMessage.NOTE_OFF) {
            return;
        }

        if (midi2LightingMapping.getMappingType() == MappingType.SIMPLE) {
            mapSimple(shortMessage, lightingUniverse);
        } else if (midi2LightingMapping.getMappingType() == MappingType.EXACT) {
            mapExact();
        }
    }

}
