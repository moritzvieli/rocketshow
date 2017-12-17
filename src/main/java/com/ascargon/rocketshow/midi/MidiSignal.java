package com.ascargon.rocketshow.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class MidiSignal {

	private int command;
	private int channel;
	private int note;
	private int velocity;

	public MidiSignal() {
	}

	public MidiSignal(ShortMessage shortMessage) {
		command = shortMessage.getCommand();
		channel = shortMessage.getChannel();
		note = shortMessage.getData1();
		velocity = shortMessage.getData2();
	}

	@XmlTransient
	public ShortMessage getShortMessage() throws InvalidMidiDataException {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(command, channel, note, velocity);
		
		return shortMessage;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

}