import { SongFile } from "./song-file";

export class SongAudioFile extends SongFile {

    device: string;

    constructor(data?: any) {
        super(data);
        
        if(!data) {
        	return;
        }

        this.device = data.device;
    }

}
