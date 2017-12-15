export class RemoteDevice {

    id: number = 0;
    name: string = "";
    host: string = "";
    synchronize: boolean = false;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.id = data.id;
        this.name = data.name;
        this.host = data.host;
        this.synchronize = data.synchronize;
    }

}
