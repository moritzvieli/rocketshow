package com.ascargon.rocketshow.raspberry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ascargon.rocketshow.util.ControlAction;
import com.pi4j.io.gpio.GpioPinDigitalInput;

@XmlRootElement
public class RaspberryGpioControl extends ControlAction {

	// A GPIO pin
	private int pinId;

    private int cyclesHigh = 0;

    private GpioPinDigitalInput button;

	@XmlElement
	public int getPinId() {
		return pinId;
	}

	public void setPinId(int pinId) {
		this.pinId = pinId;
	}

	@XmlTransient
    public GpioPinDigitalInput getButton() {
        return button;
    }

    public void setButton(GpioPinDigitalInput button) {
        this.button = button;
    }

    @XmlTransient
    public int getCyclesHigh() {
        return cyclesHigh;
    }

    public void setCyclesHigh(int cyclesHigh) {
        this.cyclesHigh = cyclesHigh;
    }

}
