export class State {
    playState: string = "STOPPED";
    currentSongIndex: number = 0;
    currentSongName: string = "";
    currentSongDurationMillis: number = 0;
    passedMillis: number;
    updateState: string;
    currentSetListName: string;
    updateFinished: boolean;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.playState = data.playState;
        this.currentSongIndex = data.currentSongIndex;
        this.currentSongName = data.currentSongName;
        this.currentSongDurationMillis = data.currentSongDurationMillis;
        this.passedMillis = data.passedMillis;
        this.updateState = data.updateState;
        this.currentSetListName = data.currentSetListName;
        this.updateFinished = data.updateFinished;
    }
}
