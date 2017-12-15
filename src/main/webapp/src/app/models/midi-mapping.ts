export class MidiMapping {
    channelOffset: number;
    noteOffset: number;

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        this.channelOffset = data.channelOffset;
        this.noteOffset = data.noteOffset;
    }
}
