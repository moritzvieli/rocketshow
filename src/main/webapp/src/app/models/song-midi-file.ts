import { SongFile } from "./song-file";
import { MidiRouting } from "./midi-routing";

export class SongMidiFile extends SongFile {

    midiRoutingList: MidiRouting[] = [];

    constructor(data?: any) {
        super(data);

        if(!data) {
        	return;
        }

        if (data.midiRoutingList) {
            for (let routing of data.midiRoutingList) {
                this.midiRoutingList.push(new MidiRouting(routing));
            }
        }
    }
}
