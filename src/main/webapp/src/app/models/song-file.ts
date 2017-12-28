export class SongFile {
    name: string;
    durationMillis: number;
    offsetMillis: number;
    active: boolean = true;
    type: string;
    isNew: boolean = false;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
        this.durationMillis = data.durationMillis;
        this.offsetMillis = data.offsetMillis;
        this.active = data.active;
    }

    stringify(): string {
        let string = JSON.stringify(this);
        let object = JSON.parse(string);

        object.isNew = undefined;

        return JSON.stringify(object);
    }

}
