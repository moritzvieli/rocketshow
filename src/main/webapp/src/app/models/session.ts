export class Session {
    firstStart: boolean = false;
    updateFinished: boolean = false;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.firstStart = data.firstStart;
        this.updateFinished = data.updateFinished;
    }
}
