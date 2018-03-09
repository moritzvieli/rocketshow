import { MidiMapping } from './midi-mapping';

export class MidiRouting {
    midiDestination: string;
    midiMapping: MidiMapping;
    remoteDeviceList: string[] = [];

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        this.midiDestination = data.midiDestination;
        this.midiMapping = new MidiMapping(data.midiMapping);

        if(data.remoteDeviceList) {
            for(let remoteDevice of data.remoteDeviceList) {
                this.remoteDeviceList.push(remoteDevice);
            }
        }
    }
}
