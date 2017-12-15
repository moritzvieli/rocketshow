import { SongFile } from "./song-file";

export class SongVideoFile extends SongFile {

    constructor(data?: any) {
        super(data);
        
        if(!data) {
        	return;
        }
    }
}
