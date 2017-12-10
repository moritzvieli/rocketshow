import { SongFile } from "./song-file";

export class SongAudioFile extends SongFile {

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        super(data);
    }
}
