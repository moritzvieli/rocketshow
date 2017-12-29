import { SongFile } from "./song-file";

export class SongAudioFile extends SongFile {

    constructor(data?: any) {
        super(data);
        
        if(!data) {
        	return;
        }
    }

}
