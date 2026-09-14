package com.ascargon.rocketshow.midi;

import com.ascargon.rocketshow.settings.Settings;
import com.ascargon.rocketshow.settings.SettingsService;
import com.ascargon.rocketshow.util.ActionExecutionService;
import com.ascargon.rocketshow.util.ActionTrigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.ShortMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultActionMidiExecutionServiceTest {

    private Settings settings;
    private ActionExecutionService actionExecutionService;
    private DefaultActionMidiExecutionService service;

    @BeforeEach
    void setUp() {
        settings = new Settings();

        SettingsService settingsService = mock(SettingsService.class);
        when(settingsService.getSettings()).thenReturn(settings);

        actionExecutionService = mock(ActionExecutionService.class);
        service = new DefaultActionMidiExecutionService(settingsService, actionExecutionService);
    }

    private <T extends ActionTriggerMidi> T addTrigger(T trigger) {
        settings.getActionTriggerMidiList().add(trigger);
        return trigger;
    }

    private void verifyTriggered(ActionTrigger trigger) throws Exception {
        verify(actionExecutionService).executeFromTrigger(trigger);
    }

    private void verifyNotTriggered() throws Exception {
        verify(actionExecutionService, never()).executeFromTrigger(any());
    }

    @Test
    void triggersOnAControlChange() throws Exception {
        ActionTriggerMidiControlChange trigger = addTrigger(new ActionTriggerMidiControlChange());
        trigger.setController(64);
        trigger.setValue(127);

        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 127);
        service.processMidiSignal(message);

        verifyTriggered(trigger);
    }

    @Test
    void aControlChangeWithoutAValueMatchesAnyValue() throws Exception {
        ActionTriggerMidiControlChange trigger = addTrigger(new ActionTriggerMidiControlChange());
        trigger.setController(64);

        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 3);
        service.processMidiSignal(message);

        verifyTriggered(trigger);
    }

    @Test
    void ignoresAControlChangeWithADifferentValue() throws Exception {
        ActionTriggerMidiControlChange trigger = addTrigger(new ActionTriggerMidiControlChange());
        trigger.setController(64);
        trigger.setValue(127);

        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 0, 64, 0);
        service.processMidiSignal(message);

        verifyNotTriggered();
    }

    @Test
    void triggersOnASongSelect() throws Exception {
        ActionTriggerMidiSongSelect trigger = addTrigger(new ActionTriggerMidiSongSelect());
        trigger.setSong(5);

        ShortMessage message = new ShortMessage();
        message.setMessage(0xF3, 5, 0);
        service.processMidiSignal(message);

        verifyTriggered(trigger);
    }

    @Test
    void triggersOnTheTransportMessages() throws Exception {
        ActionTriggerMidiSystemRealTime trigger = addTrigger(new ActionTriggerMidiSystemRealTime());
        trigger.setSystemRealTimeType(ActionTriggerMidiSystemRealTime.SystemRealTimeType.CONTINUE);

        ShortMessage message = new ShortMessage();
        message.setMessage(0xFB);
        service.processMidiSignal(message);

        verifyTriggered(trigger);
    }

    @Test
    void ignoresATransportMessageOfAnotherType() throws Exception {
        ActionTriggerMidiSystemRealTime trigger = addTrigger(new ActionTriggerMidiSystemRealTime());
        trigger.setSystemRealTimeType(ActionTriggerMidiSystemRealTime.SystemRealTimeType.START);

        ShortMessage message = new ShortMessage();
        message.setMessage(0xFC);
        service.processMidiSignal(message);

        verifyNotTriggered();
    }

    /**
     * A system message has no channel: the low nibble of 0xF3 is part of the status byte, but
     * ShortMessage#getChannel() still reports it as channel 3.
     */
    @Test
    void theChannelOfATriggerDoesNotApplyToSystemMessages() throws Exception {
        ActionTriggerMidiSongSelect trigger = addTrigger(new ActionTriggerMidiSongSelect());
        trigger.setChannel(0);
        trigger.setSong(5);

        ShortMessage message = new ShortMessage();
        message.setMessage(0xF3, 5, 0);
        service.processMidiSignal(message);

        verifyTriggered(trigger);
    }

    @Test
    void theChannelStillAppliesToChannelMessages() throws Exception {
        ActionTriggerMidiNoteOn trigger = addTrigger(new ActionTriggerMidiNoteOn());
        trigger.setChannel(2);
        trigger.setNote(60);

        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        service.processMidiSignal(message);

        verifyNotTriggered();
    }

}
