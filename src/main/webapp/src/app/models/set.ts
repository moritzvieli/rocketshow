import { Composition } from './composition'; 

export class Set {
    currentCompositionIndex: number;
    compositionList: Composition[] = [];
    name: string;
    notes: string;

    constructor(data?: any) {
        if(!data) {
        	return;
        }
        
        this.currentCompositionIndex = data.currentCompositionIndex;
        this.notes = data.notes;
        this.name = data.name;

        this.compositionList = [];

        if(data.compositionList) {
            for(let composition of data.compositionList) {
                this.compositionList.push(new Composition(composition));
            }
        }
    }

    stringify(): string {
        let string = JSON.stringify(this);
        let object = JSON.parse(string);

        object.compositionList = [];
        object.currentCompositionIndex = undefined;

        if(this.compositionList) {
            for (let composition of this.compositionList) {
                let compositionObj: any = {};
                compositionObj.name = composition.name;
                compositionObj.durationMillis = composition.durationMillis;
                compositionObj.autoStartNextComposition = composition.autoStartNextComposition;

                object.compositionList.push(compositionObj);
            }
        }

        return JSON.stringify(object);
    }
}
