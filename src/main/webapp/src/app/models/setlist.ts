import { Song } from './song'; 

export class SetList {
    currentSongIndex: number;
    songList: Song[];
    name: string;
    notes: string;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.currentSongIndex = data.currentSongIndex;
        this.notes = data.notes;
        this.name = data.name;

        this.songList = [];

        if(data.songList) {
            for(let song of data.songList) {
                this.songList.push(new Song(song));
            }
        }
    }

    stringify(): string {
        let string = JSON.stringify(this);
        let object = JSON.parse(string);

        object.songList = [];

        for (let song of this.songList) {
            let songObj: any = {};
            songObj.name = song.name;
            songObj.durationMillis = song.durationMillis;
            songObj.autoStartNextSong = song.autoStartNextSong;

            object.songList.push(songObj);
        }

        return JSON.stringify(object);
    }
}
