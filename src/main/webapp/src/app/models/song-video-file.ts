import { SongFile } from "./song-file";

export class SongVideoFile extends SongFile {

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        super(data);
    }
}
