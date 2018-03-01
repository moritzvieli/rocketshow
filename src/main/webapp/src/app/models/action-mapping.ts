export class ActionMapping {
    midiAction: string = 'PLAY';
    channelFrom: number = 0;
    noteFrom: number = 0;
    remoteDeviceList: number[] = [];
    executeLocally: boolean = true;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.midiAction = data.midiAction;
        this.channelFrom = data.channelFrom;
        this.noteFrom = data.noteFrom;

        if(data.remoteDeviceList) {
            for(let remoteDevice of data.remoteDeviceList) {
                this.remoteDeviceList.push(remoteDevice);
            }
        }

        this.executeLocally = data.executeLocally;
    }
}
