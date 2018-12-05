package com.ascargon.rocketshow.raspberry;

import com.ascargon.rocketshow.Manager;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RaspberryGpioControlActionExecuter {

    private GpioController gpioController;

    final static Logger logger = Logger.getLogger(RaspberryGpioControlActionExecuter.class);

    private HashMap<Integer, Timer> executionTimerList = new HashMap<>();

    private HashMap<GpioPinDigitalInput, Integer> highCycles = new HashMap<>();
    private Timer detectTimer = new Timer();

    public RaspberryGpioControlActionExecuter(Manager manager) {
        if (!manager.getSettings().isEnableRaspberryGpio()) {
            return;
        }

        // Initialize the instance
        gpioController = GpioFactory.getInstance();

        // Add a button for each configured control
        for (RaspberryGpioControl raspberryGpioControl : manager.getSettings().getRaspberryGpioControlList()) {
            GpioPinDigitalInput button = gpioController.provisionDigitalInputPin(
                    getPinFromId(raspberryGpioControl.getPinId()));

            button.setShutdownOptions(true);

            button.setDebounce(manager.getSettings().getRaspberryGpioDebounceMillis());


//            RaspberryGpioControlButton raspberryGpioControlButton = new RaspberryGpioControlButton();
//            raspberryGpioControlButton.setRaspberryGpioControl(raspberryGpioControl);
//            raspberryGpioControlButton.setButton(button);
//
//            raspberryGpioControlButtonList.add(raspberryGpioControlButton);
//
//            highCycles.put(button, 0);
//
//            raspberryGpioControl.setButton(button);

            GpioPinListenerDigital listener = event -> {
                if (event.getState().isHigh()) {
                    logger.debug("Input high from GPIO " + event.getPin() + " recognized");

                    try {
                        manager.getControlActionExecuter().execute(raspberryGpioControl);
                    } catch (Exception e) {
                        logger.error("Could not execute action from Raspberry GPIO", e);
                    }
                }
            };

            // Add the same listener for all pins, because a no-class-def found
            // error will raise, when
            // added
            button.addListener(listener);
        }

        // Don't use the listener as recommended, because it will deliver random
        // ghost bounces on instable power networks. Also with correctly implemented
        // pull-down resistors against floating.
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                for (RaspberryGpioControl raspberryGpioControl : manager.getSettings().getRaspberryGpioControlList()) {
//                    if (raspberryGpioControl.getButton().getState() == PinState.HIGH) {
//                        raspberryGpioControl.setCyclesHigh(raspberryGpioControl.getCyclesHigh() + 1);
//
//                        if (raspberryGpioControl.getCyclesHigh() >= 3) {
//                            try {
//                                //logger.debug("PLAYING COMPOSITION FOR " + raspberryGpioControl.getPinId());
//                                manager.getControlActionExecuter().execute(raspberryGpioControl);
//                            } catch (Exception e) {
//                                logger.error("Could not execute action from Raspberry GPIO", e);
//                            }
//
//                            raspberryGpioControl.setCyclesHigh(0);
//                        }
//                    } else {
//                        raspberryGpioControl.setCyclesHigh(0);
//                    }
//                }
//            }
//        };
//
//        detectTimer.schedule(timerTask, 20, 20);
    }

    // RaspiPin.getPinByAddress does not work ("read error: no device found")
    private Pin getPinFromId(int pinId) {
        switch (pinId) {
            case 0:
                return RaspiPin.GPIO_00;
            case 1:
                return RaspiPin.GPIO_01;
            case 2:
                return RaspiPin.GPIO_02;
            case 3:
                return RaspiPin.GPIO_03;
            case 4:
                return RaspiPin.GPIO_04;
            case 5:
                return RaspiPin.GPIO_05;
            case 6:
                return RaspiPin.GPIO_06;
            case 7:
                return RaspiPin.GPIO_07;
            case 10:
                return RaspiPin.GPIO_10;
            case 11:
                return RaspiPin.GPIO_11;
            case 12:
                return RaspiPin.GPIO_12;
            case 13:
                return RaspiPin.GPIO_13;
            case 14:
                return RaspiPin.GPIO_14;
            case 15:
                return RaspiPin.GPIO_15;
            case 16:
                return RaspiPin.GPIO_16;
            case 17:
                return RaspiPin.GPIO_17;
            case 18:
                return RaspiPin.GPIO_18;
            case 19:
                return RaspiPin.GPIO_19;
            case 20:
                return RaspiPin.GPIO_20;
            case 21:
                return RaspiPin.GPIO_21;
            case 22:
                return RaspiPin.GPIO_22;
            case 23:
                return RaspiPin.GPIO_23;
            case 24:
                return RaspiPin.GPIO_24;
            case 25:
                return RaspiPin.GPIO_25;
            case 26:
                return RaspiPin.GPIO_26;
            case 27:
                return RaspiPin.GPIO_27;
            case 28:
                return RaspiPin.GPIO_28;
            case 29:
                return RaspiPin.GPIO_29;
        }

        return null;
    }

    public void close() {
        if (gpioController != null) {
            gpioController.shutdown();
        }

        detectTimer.cancel();
        detectTimer = null;
    }

}
