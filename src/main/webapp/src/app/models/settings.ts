import { MidiControl } from './midi-control';
import { MidiDevice } from "./midi-device";
import { RemoteDevice } from "./remote-device";

export class Settings {
    midiInDevice: MidiDevice;
    midiOutDevice: MidiDevice;
    remoteDeviceList: RemoteDevice[];
    midiControlList: MidiControl[];
    dmxSendDelayMillis: number;
    defaultComposition: string;
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

            for(let remoteDevice of data.remoteDeviceList) {
                this.remoteDeviceList.push(new RemoteDevice(remoteDevice));
            }
        }
        if(data.midiControlList) {
            this.midiControlList = [];

            for(let midiControl of data.midiControlList) {
                this.midiControlList.push(new MidiControl(midiControl));
            }
        }
        this.dmxSendDelayMillis = data.dmxSendDelayMillis;
        this.defaultComposition = data.defaultComposition;
        this.offsetMillisMidi = data.offsetMillisMidi;
        this.offsetMillisAudio = data.offsetMillisAudio;
        this.audioPlayerType = data.audioPlayerType;
        this.loggingLevel = data.loggingLevel;
        this.language = data.language;
        this.deviceName = data.deviceName;
        this.resetUsbAfterBoot = data.resetUsbAfterBoot;
    }

}
