import { SongFile } from "./song-file";

export class SongMidiFile extends SongFile {

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        super(data);
    }
}
