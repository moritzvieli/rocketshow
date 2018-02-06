import { SongFile } from "./song-file";

export class SongAudioFile extends SongFile {

    outputBus: string;

    constructor(data?: any) {
        super(data);
        
        if(!data) {
        	return;
        }

        this.outputBus = data.outputBus;
    }

}
