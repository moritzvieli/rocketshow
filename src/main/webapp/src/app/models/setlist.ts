import { Song } from './song'; 

export class SetList {
    name: string;
    songList: Song[];
    currentSongIndex: number;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.currentSongIndex = data.currentSongIndex;

        this.songList = [];

        for(let song of data.songList) {
            this.songList.push(new Song(song));
        }
    }
}
