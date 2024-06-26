package com.ascargon.rocketshow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@Service
public class DefaultOperatingSystemInformationService implements OperatingSystemInformationService {

    private final static Logger logger = LoggerFactory.getLogger(DefaultOperatingSystemInformationService.class);

    private final OperatingSystemInformation operatingSystemInformation = new OperatingSystemInformation();

    public DefaultOperatingSystemInformationService() {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        String architectureType = System.getProperty("sun.arch.data.model").toLowerCase();

        if (operatingSystem.contains("windows")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.WINDOWS);
        } else if (operatingSystem.contains("mac os x")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.OS_X);
        } else if (operatingSystem.contains("linux")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.LINUX);
        } else if (operatingSystem.contains("hp-ux")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.HP_UNIX);
        } else if (operatingSystem.contains("hpux")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.HP_UNIX);
        } else if (operatingSystem.contains("solaris")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.SOLARIs);
        } else if (operatingSystem.contains("sunos")) {
            operatingSystemInformation.setType(OperatingSystemInformation.Type.SUN_OS);
        }

        if (architectureType.equals("32")) {
            operatingSystemInformation.setArchitectureType(OperatingSystemInformation.ArchitectureType.T32);
        } else if (architectureType.equals("64")) {
            operatingSystemInformation.setArchitectureType(OperatingSystemInformation.ArchitectureType.T64);
        }

        if (operatingSystemInformation.getType().equals(OperatingSystemInformation.Type.LINUX)) {
            final File file = new File("/etc", "os-release");
            String idLike = "";
            String id = "";

            try (FileInputStream fileInputStream = new FileInputStream(file);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.toLowerCase().startsWith("id_like")) {
                        idLike = line.split("=")[1];
                    } else if (line.toLowerCase().startsWith("id")) {
                        id = line.split("=")[1];
                    }
                }
            } catch (final Exception e) {
                logger.error("Could not read details about the linux operating system", e);
            }

            if (idLike.isEmpty()) {
                // id_like could not be parsed. this might happen on some distros. try the id.
                if ("debian".equals(id)) {
                    operatingSystemInformation.setSubType(OperatingSystemInformation.SubType.DEBIAN);

                    // check a file generated by pi-gen
                    // https://forums.raspberrypi.com/viewtopic.php?t=331920
                    File fileRpiIssue = new File("/etc", "rpi-issue");
                    if (fileRpiIssue.exists()) {
                        operatingSystemInformation.setSubType(OperatingSystemInformation.SubType.RASPBERRYOS);
                        determineRaspberryModel(operatingSystemInformation);
                    }
                }
            } else if ("debian".equals(idLike)) {
                if ("raspbian".equals(id)) {
                    operatingSystemInformation.setSubType(OperatingSystemInformation.SubType.RASPBERRYOS);
                    determineRaspberryModel(operatingSystemInformation);
                } else {
                    operatingSystemInformation.setSubType(OperatingSystemInformation.SubType.DEBIAN);
                }
            } else if ("ubuntu".equals(idLike)) {
                operatingSystemInformation.setSubType(OperatingSystemInformation.SubType.UBUNTU);
            }
        }
    }

    private void determineRaspberryModel(OperatingSystemInformation operatingSystemInformation) {
        // get the Raspberry model
        final File file = new File("/sys/firmware/devicetree/base", "model");

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {

            String line = bufferedReader.readLine();

            if (line.startsWith("Raspberry Pi 3")) {
                operatingSystemInformation.setRaspberryVersion(OperatingSystemInformation.RaspberryVersion.MODEL_3);
            } else if (line.startsWith("Raspberry Pi 4")) {
                operatingSystemInformation.setRaspberryVersion(OperatingSystemInformation.RaspberryVersion.MODEL_4);
            } else if (line.startsWith("Raspberry Pi 5")) {
                operatingSystemInformation.setRaspberryVersion(OperatingSystemInformation.RaspberryVersion.MODEL_5);
            }
        } catch (final Exception e) {
            logger.error("Could not read details about the Raspberry model", e);
        }
    }

    @Override
    public OperatingSystemInformation getOperatingSystemInformation() {
        return operatingSystemInformation;
    }

}
