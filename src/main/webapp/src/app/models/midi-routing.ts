import { MidiMapping } from './midi-mapping';

export class MidiRouting {
    midiDestination: string;
    midiMapping: MidiMapping;

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        this.midiDestination = data.midiDestination;
       
        this.midiMapping = new MidiMapping(data.midiMapping);
    }
}
