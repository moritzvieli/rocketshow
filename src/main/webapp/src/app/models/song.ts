export class Song {
    name: string;
    durationMillis: number;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.durationMillis = data.durationMillis;
    }
}
