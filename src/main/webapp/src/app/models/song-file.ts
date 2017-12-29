export class SongFile {
    name: string;
    durationMillis: number;
    offsetMillis: number;
    active: boolean = true;
    type: string;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.durationMillis = data.durationMillis;
        this.offsetMillis = data.offsetMillis;
        this.active = data.active;
        this.type = data.type;
    }

    stringify(): string {
        let string = JSON.stringify(this);
        let object = JSON.parse(string);

        return JSON.stringify(object);
    }

}
