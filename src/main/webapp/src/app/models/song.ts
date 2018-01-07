import { SongVideoFile } from './song-video-file';
import { SongMidiFile } from "./song-midi-file";
import { SongAudioFile } from "./song-audio-file";
import { SongFile } from "./song-file";

export class Song {
    name: string;
    durationMillis: number;
    fileList: SongFile[] = [];
    notes: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.name = data.name;
        this.durationMillis = data.durationMillis;
        this.notes = data.notes;

        this.fileList = this.parseFileList(data);
    }

    private parseFileList(data: any): SongFile[] {
        let fileList: SongFile[] = [];

        if (data.fileList) {
            for (let file of data.fileList) {
                if (file.midiFile) {
                    let midiFile = new SongMidiFile(file.midiFile);
                    fileList.push(midiFile);
                } else if (file.audioFile) {
                    let audioFile = new SongAudioFile(file.audioFile);
                    fileList.push(audioFile);
                } else if (file.videoFile) {
                    let videoFile = new SongVideoFile(file.videoFile);
                    fileList.push(videoFile);
                }
            }
        }

        return fileList;
    }

    // Return a file object based on its type
    public static getFileObjectByType(data: any) {
        if (data.type == 'MIDI') {
            let midiFile = new SongMidiFile(data);
            return midiFile;
        } else if (data.type == 'AUDIO') {
            let audioFile = new SongAudioFile(data);
            return audioFile;
        } else if (data.type == 'VIDEO') {
            let videoFile = new SongVideoFile(data);
            return videoFile;
        }
    }

    // Stringify the song and it's files correct (JSON would ignore the extended file classes by default)
    stringify(): string {
        let string = JSON.stringify(this);
        let object = JSON.parse(string);

        object.fileList = [];

        for (let file of this.fileList) {
            if (file instanceof SongMidiFile) {
                let fileObj: any = {};
                fileObj.midiFile = file;
                object.fileList.push(fileObj);
            } else if (file instanceof SongAudioFile) {
                let fileObj: any = {};
                fileObj.audioFile = file;
                object.fileList.push(fileObj);
            } else if (file instanceof SongVideoFile) {
                let fileObj: any = {};
                fileObj.videoFile = file;
                object.fileList.push(fileObj);
            }
        }

        return JSON.stringify(object);
    }
}
