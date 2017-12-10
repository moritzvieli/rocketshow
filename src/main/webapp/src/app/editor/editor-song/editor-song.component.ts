import { SongFile } from './../../models/song-file';
import { EditorSongFileComponent } from './editor-song-file/editor-song-file.component';
import { Song } from './../../models/song';
import { SongService } from './../../services/song.service';
import { Component, OnInit } from '@angular/core';
import { NgModel } from '@angular/forms';
import { SongVideoFile } from './../../models/song-video-file';
import { SongMidiFile } from "./../../models/song-midi-file";
import { SongAudioFile } from "./../../models/song-audio-file";
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';

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

  constructor(
    private songService: SongService,
    private modalService: BsModalService) {
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
    if (!searchValue) {
      this.filteredSongs = this.songs;
      return;
    }

    this.filteredSongs = [];

    for (let song of this.songs) {
      if (song.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
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

  // Delete the song
  deleteSong(song: Song) {
    // TODO
  }

  // Add a new file to the song
  addSongFile() {
    // TODO
  }

  // Edit a song file's details
  editSongFileDetails(file: SongFile) {
    // Create a backup of the current song
    let oldSong: Song = new Song(JSON.parse(this.currentSong.stringify()));

    // Show the file details dialog
    let fileDialog = this.modalService.show(EditorSongFileComponent, { animated: true, keyboard: true, backdrop: true, ignoreBackdropClick: true });
    (<EditorSongFileComponent>fileDialog.content).file = file;

    (<EditorSongFileComponent>fileDialog.content).onClose.subscribe(result => {
      if (result !== true) {
        // Cancel has been pressed -> reset the file before the dialog
        this.currentSong.fileList = oldSong.fileList;
      }
    });
  }

  // Test the file types for the template
  isMidiFile(file: SongFile): boolean {
    if (file instanceof SongMidiFile) {
      return true;
    }

    return false;
  }

  isAudioFile(file: SongFile): boolean {
    if (file instanceof SongAudioFile) {
      return true;
    }

    return false;
  }

  isVideoFile(file: SongFile): boolean {
    if (file instanceof SongVideoFile) {
      return true;
    }

    return false;
  }

}
