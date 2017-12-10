import { SongVideoFile } from './song-video-file';
import { SongMidiFile } from "./song-midi-file";
import { SongAudioFile } from "./song-audio-file";
import { SongFile } from "./song-file";

export class Song {
    name: string;
    durationMillis: number;
    isNew: boolean = false;
    fileList: SongFile[];

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.name = data.name;
        this.durationMillis = data.durationMillis;

        this.fileList = [];

        if (data.fileList) {
            for (let file of data.fileList) {
                if (file.midiFile) {
                    this.fileList.push(new SongMidiFile(file.midiFile));
                } else if (file.audioFile) {
                    this.fileList.push(new SongAudioFile(file.audioFile));
                } else if (file.videoFile) {
                    this.fileList.push(new SongVideoFile(file.videoFile));
                }
            }
        }
    }
}
