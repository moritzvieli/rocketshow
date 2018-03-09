export class CompositionFile {
    name: string;
    durationMillis: number;
    offsetMillis: number = 0;
    active: boolean = true;
    type: string;
    loop: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.name = data.name;
        this.durationMillis = data.durationMillis;
        this.offsetMillis = data.offsetMillis;
        this.active = data.active;
        this.type = data.type;
        this.loop = data.loop;
    }

    getFontAwesomeIconClass() {
        switch (this.type) {
            case 'MIDI': {
                return 'fa-music';
            }
            case 'AUDIO': {
                return 'fa-microphone';
            }
            case 'VIDEO': {
                return 'fa-film';
            }
            default: {
                return 'fa-question';
            }
        }
    }

}