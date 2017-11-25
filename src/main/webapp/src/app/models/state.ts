export class State {
    playState: string = "STOPPED";
    currentSongIndex: number = 0;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.playState = data.playState;
        this.currentSongIndex = data.currentSongIndex;
    }
}
