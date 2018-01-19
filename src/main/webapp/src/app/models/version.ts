import { ChangeNote } from "./changeNote";

export class Version {
    version: string;
    date: Date;
    changeNoteList: ChangeNote[];

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        this.version = data.version;
        this.date = new Date(data.date);

        this.changeNoteList = [];

        if(data.changeNoteList) {
            for(let changeNote of data.changeNoteList) {
                this.changeNoteList.push(new ChangeNote(changeNote));
            }
        }
    }
}
