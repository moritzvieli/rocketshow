export class Settings {
    language: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.language = data.language;
    }

}
