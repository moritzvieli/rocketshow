import { MidiMapping } from './midi-mapping';
import { MidiDevice } from "./midi-device";
import { RemoteDevice } from "./remote-device";

export class Settings {
    midiInDevice: MidiDevice;
    midiOutDevice: MidiDevice;
    remoteDeviceList: RemoteDevice[];
    dmxSendDelayMillis: number;
    idleSong: string;
    offsetMillisMidi: number;
    offsetMillisAudio: number;
    offsetMillisVideo: number;
    audioPlayerType: string;
    loggingLevel: string;
    language: string;
    deviceName: string;
    resetUsbAfterBoot: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        if(data.midiInDevice) {
            this.midiInDevice = new MidiDevice(data.midiInDevice);
        }
        if(data.midiOutDevice) {
            this.midiInDevice = new MidiDevice(data.midiOutDevice);
        }
        if(data.remoteDeviceList) {
            this.remoteDeviceList = [];

            for(let device of data.remoteDeviceList) {
                this.remoteDeviceList.push(new RemoteDevice(device));
            }
        }
        this.dmxSendDelayMillis = data.dmxSendDelayMillis;
        this.idleSong = data.idleSong;
        this.offsetMillisMidi = data.offsetMillisMidi;
        this.offsetMillisAudio = data.offsetMillisAudio;
        this.audioPlayerType = data.audioPlayerType;
        this.loggingLevel = data.loggingLevel;
        this.language = data.language;
        this.deviceName = data.deviceName;
        this.resetUsbAfterBoot = data.resetUsbAfterBoot;
    }

}
