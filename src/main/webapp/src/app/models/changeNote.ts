export class ChangeNote {
    version: string;
    changes: string;

    constructor(data?: any) {
        if(!data) {
        	return;
        }

        this.version = data.version;
        this.changes = data.changes;
    }
}
