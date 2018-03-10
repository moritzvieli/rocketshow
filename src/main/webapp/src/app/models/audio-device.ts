export class AudioDevice {
    id: number = 0;
    key: string = '';
    name: string = '';

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.id = data.id;
        this.key = data.key;
        this.name = data.name;
    }
}
