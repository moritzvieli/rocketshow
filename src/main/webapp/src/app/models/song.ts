export class Song {
    name: string;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.name = data.name;
    }
}
