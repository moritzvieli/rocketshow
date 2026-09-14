package com.ascargon.rocketshow.api;

import com.ascargon.rocketshow.composition.CompositionService;
import com.ascargon.rocketshow.composition.SetService;
import com.ascargon.rocketshow.midi.MidiTimecodeSlaveService;
import com.ascargon.rocketshow.play.PlayerService;
import org.springframework.stereotype.Service;

@Service
public class DefaultStateService implements StateService {

    private final MidiTimecodeSlaveService midiTimecodeSlaveService;

    public DefaultStateService(MidiTimecodeSlaveService midiTimecodeSlaveService) {
        this.midiTimecodeSlaveService = midiTimecodeSlaveService;
    }

    private int getCompositionIndexWithoutSet(
            CompositionService compositionService,
            String compositionName
    ) {
        return compositionService.getCompositionIndex(compositionName);
    }

    @Override
    public State getCurrentState(
            PlayerService playerService,
            SetService setService,
            CompositionService compositionService
    ) {
        State currentState = new State();

        currentState.setMidiTimecodeLocked(midiTimecodeSlaveService.isLocked());

        long midiTimecodeMillis = midiTimecodeSlaveService.getTimecodeMillis();

        if (midiTimecodeMillis >= 0) {
            currentState.setMidiTimecodeMillis(midiTimecodeMillis);
        }

        if (playerService != null) {
            currentState.setPlayState(playerService.getPlayState());
            currentState.setCurrentCompositionName(playerService.getCompositionName());
            currentState.setCurrentCompositionDurationMillis(playerService.getCompositionDurationMillis());
            currentState.setPositionMillis(playerService.getPositionMillis());
        }

        if (setService == null) {
            if (playerService != null) {
                currentState.setCurrentCompositionIndex(getCompositionIndexWithoutSet(compositionService, playerService.getCompositionName()));
            }
        } else {
            currentState.setCurrentCompositionIndex(setService.getCurrentCompositionIndex());

            if (setService.getCurrentSet() == null && playerService != null) {
                currentState.setCurrentCompositionIndex(getCompositionIndexWithoutSet(compositionService, playerService.getCompositionName()));
            } else {
                currentState.setCurrentSetName(setService.getCurrentSet().getName());
            }
        }

        return currentState;
    }

}
