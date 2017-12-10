import { Song } from './../../models/song';
import { SongService } from './../../services/song.service';
import { Component, OnInit } from '@angular/core';
import { NgModel } from '@angular/forms';
import { SongVideoFile } from './../../models/song-video-file';
import { SongMidiFile } from "./../../models/song-midi-file";
import { SongAudioFile } from "./../../models/song-audio-file";
import { SongFile } from '../../models/song-file';

@Component({
  selector: 'app-editor-song',
  templateUrl: './editor-song.component.html',
  styleUrls: ['./editor-song.component.scss']
})
export class EditorSongComponent implements OnInit {

  searchName: string = '';

  loadingSong: boolean = false;

  private songs: Song[];
  filteredSongs: Song[];
  currentSong: Song;
  files: any[] = [];

  constructor(private songService: SongService) {
    var file1: any = {};
    file1.name = 'wise_guy.mid';
    file1.type = 'midi';
    this.files.push(file1);

    var file2: any = {};
    file2.name = 'wise_guy_click.wav';
    file2.type = 'audio';
    this.files.push(file2);

    var file3: any = {};
    file3.name = 'wise_guy.mp4';
    file3.type = 'video';
    this.files.push(file3);
  }

  ngOnInit() {
    this.songService.getSongs().subscribe((songs: Song[]) => {
      this.songs = songs;
      this.filterSongs();
    });
  }

  // Prevent the last item in the file-list to be draggable.
  // Taken from http://jsbin.com/tuyafe/1/edit?html,js,output
  sortMove(evt) {
    return evt.related.className.indexOf('no-sortjs') === -1;
  }

  // Filter the song list
  filterSongs(searchValue?: string) {
    if(!searchValue) {
      this.filteredSongs = this.songs;
      return;
    }

    this.filteredSongs = [];

    for(let song of this.songs) {
      if(song.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredSongs.push(song);
      }
    }
  }

  // Select a song
  selectSong(song: Song) {
    // Load the details of the selected song
    this.loadingSong = true;

    this.songService.loadSong(song.name).subscribe((song: Song) => {
      this.currentSong = song;
      this.loadingSong = false;
    });
  }

  // Unselect a song
  unselectSong() {
    this.currentSong = undefined;
  }

  // Create a new song
  createSong() {
    this.currentSong = new Song();
    this.currentSong.isNew = true;
  }

  // Save a new song
  saveSong(song: Song) {
    // TODO
  }

  // Add a new file to the song
  addSongFile() {
    // TODO
  }

  // Edit a song file's details
  editSongFileDetails(file: SongFile) {
    // TODO
  }

  // Test the file types for the template
  isMidiFile(file: SongFile): boolean {
    if(file instanceof SongMidiFile) {
      return true;
    }

    return false;
  }

  isAudioFile(file: SongFile): boolean {
    if(file instanceof SongAudioFile) {
      return true;
    }

    return false;
  }

  isVideoFile(file: SongFile): boolean {
    if(file instanceof SongVideoFile) {
      return true;
    }

    return false;
  }

}
