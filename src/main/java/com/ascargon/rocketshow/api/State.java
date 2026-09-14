package com.ascargon.rocketshow.api;

import com.ascargon.rocketshow.play.CompositionPlayer;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
public class State {

    private Integer currentCompositionIndex;
    private CompositionPlayer.PlayState playState;
    private String currentCompositionName;
    private Long currentCompositionDurationMillis;
    private Long positionMillis;
    private String currentSetName;

    // True while this device is following an incoming MIDI timecode, and the absolute position of
    // that timecode master
    private boolean midiTimecodeLocked;
    private Long midiTimecodeMillis;

    private String error;

}
