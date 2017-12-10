export class SongFile {
    name: string;
    durationMillis: number;
    offsetMillis: number;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.durationMillis = data.durationMillis;
        this.offsetMillis = data.offsetMillis;
    }
}
