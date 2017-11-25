export class SetList {
    name: string;
    songList: string[];
    currentSongIndex: number;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.currentSongIndex = data.currentSongIndex;

        this.songList = [];

        for(let song of data.songList) {
            this.songList.push(song);
        }
    }
}
