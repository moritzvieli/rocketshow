export class State {
    playState: string = "STOPPED";
    currentSongIndex: number = 0;
    currentSongName: string = "";
    currentSongDurationMillis: number = 0;
    lastStartTime: Date;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.playState = data.playState;
        this.currentSongIndex = data.currentSongIndex;
        this.currentSongName = data.currentSongName;
        this.currentSongDurationMillis = data.currentSongDurationMillis;
        this.lastStartTime = new Date(data.lastStartTime);
    }
}
