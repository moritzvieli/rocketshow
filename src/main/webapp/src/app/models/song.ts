import { SongVideoFile } from './song-video-file';
import { SongMidiFile } from "./song-midi-file";
import { SongAudioFile } from "./song-audio-file";
import { SongFile } from "./song-file";

export class Song {
    name: string;
    durationMillis: number;
    isNew: boolean = false;
    fileList: SongFile[] = [];

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.name = data.name;
        this.durationMillis = data.durationMillis;

        this.fileList = this.parseFileList(data);
    }

    private parseFileList(data: any): SongFile[] {
        let fileList: SongFile[] = [];

        if (data.fileList) {
            for (let file of data.fileList) {
                if (file.midiFile) {
                    fileList.push(new SongMidiFile(file.midiFile));
                } else if (file.audioFile) {
                    fileList.push(new SongAudioFile(file.audioFile));
                } else if (file.videoFile) {
                    fileList.push(new SongVideoFile(file.videoFile));
                }
            }
        }

        return fileList;
    }

    // Stringify the song and it's files correct (JSON would ignore the extended file classes by default)
    stringify(): string {
        let songString = JSON.stringify(this);
        let songObject = JSON.parse(songString);

        songObject.fileList = [];

        for (let file of this.fileList) {
            if (file instanceof SongMidiFile) {
                let fileObj: any = {};
                fileObj.midiFile = file;
                songObject.fileList.push(fileObj);
            } else if (file instanceof SongAudioFile) {
                let fileObj: any = {};
                fileObj.audioFile = file;
                songObject.fileList.push(fileObj);
            } else if (file instanceof SongVideoFile) {
                let fileObj: any = {};
                fileObj.videoFile = file;
                songObject.fileList.push(fileObj);
            }
        }

        return JSON.stringify(songObject);
    }
}
