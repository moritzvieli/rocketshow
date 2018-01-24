import { WarningDialogService } from './../../services/warning-dialog.service';
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
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { PendingChangesDialogService } from '../../services/pending-changes-dialog.service';
import { Observable } from 'rxjs/Observable';
import { SortablejsOptions } from 'angular-sortablejs/dist';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

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

  // The song, as it was when we loaded it
  initialSong: Song;

  constructor(
    private songService: SongService,
    private modalService: BsModalService,
    private warningDialogService: WarningDialogService,
    private pendingChangesDialogService: PendingChangesDialogService,
    private toastrService: ToastrService,
    private translateService: TranslateService) {
  }

  ngOnInit() {
    this.loadSongs();
  }

  private loadSongs() {
    this.songService.getSongs(true).subscribe((songs: Song[]) => {
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

  private copyInitialSong() {
    this.initialSong = new Song(JSON.parse(this.currentSong.stringify()));
  }

  checkPendingChanges(): Observable<boolean> {
    return this.pendingChangesDialogService.check(this.initialSong, this.currentSong, 'editor.warning-song-changes');
  }

  // Select a song
  selectSong(song: Song) {
    if (this.currentSong && this.currentSong.name == song.name) {
      return;
    }

    this.checkPendingChanges().map(result => {
      if (result) {
        // Load the details of the selected song
        this.loadingSong = true;

        this.songService.getSong(song.name).subscribe((song: Song) => {
          this.currentSong = song;

          this.copyInitialSong();
          this.loadingSong = false;
        });
      }
    }).subscribe();
  }

  // Unselect a song
  unselect() {
    this.currentSong = undefined;
    this.initialSong = undefined;
  }

  // Create a new song
  createSong() {
    this.currentSong = new Song();
    this.copyInitialSong();
  }

  private saveApi(song: Song) {
    this.songService.saveSong(song).map(() => {
      this.loadSongs();
      this.copyInitialSong();

      this.songService.songsChanged.next();

      this.translateService.get(['editor.toast-song-save-success', 'editor.toast-save-success-title']).subscribe(result => {
        this.toastrService.success(result['editor.toast-song-save-success'], result['editor.toast-save-success-title']);
      });
    }).subscribe();
  }

  // Save a new song
  save(song: Song) {
    // Delete the old song, if the name changed
    if (this.initialSong && this.initialSong.name && this.initialSong.name != song.name && this.initialSong.name.length > 0) {
      this.songService.deleteSong(this.initialSong.name).map(() => {
        this.saveApi(song);
      }).subscribe();
    } else {
      this.saveApi(song);
    }
  }

  // Delete the song
  delete(song: Song) {
    this.warningDialogService.show('editor.warning-delete-song').map(result => {
      if (result) {
        this.songService.deleteSong(this.initialSong.name).map(() => {
          this.unselect();
          this.loadSongs();

          this.songService.songsChanged.next();

          this.translateService.get(['editor.toast-song-delete-success', 'editor.toast-delete-success-title']).subscribe(result => {
            this.toastrService.success(result['editor.toast-song-delete-success'], result['editor.toast-delete-success-title']);
          });
        }).subscribe();
      }
    }).subscribe();
  }

  // Add a new file to the song
  addSongFile() {
    this.editSongFileDetails(0, true);
  }

  // Toggle the active state (mute)
  toggleActive(file: SongFile) {
    file.active = !file.active;
  }

  private rebuildFileListBasedOnType() {
    // Ensure, the file objects are of correct instance based on their type.
    // The type may be changed in the choose file dialog.
    let newFileList: SongFile[] = [];

    for (let file of this.currentSong.fileList) {
      let newFile = Song.getFileObjectByType(file);
      newFileList.push(newFile);
    }

    this.currentSong.fileList = newFileList;
  }

  deleteFile(fileIndex: number) {
    this.currentSong.fileList.splice(fileIndex, 1);
  }

  // Edit a song file's details
  editSongFileDetails(fileIndex: number, addNew: boolean = false) {
    // Create a backup of the current song
    let songCopy: Song = new Song(JSON.parse(this.currentSong.stringify()));

    if (addNew) {
      // Add a new file, if necessary
      let newFile: SongFile = new SongFile();
      songCopy.fileList.push(newFile);
      fileIndex = songCopy.fileList.length - 1;
    }

    // Show the file details dialog
    let fileDialog = this.modalService.show(EditorSongFileComponent, { keyboard: true, ignoreBackdropClick: true, class: 'modal-lg' });
    (<EditorSongFileComponent>fileDialog.content).fileIndex = fileIndex;
    (<EditorSongFileComponent>fileDialog.content).file = songCopy.fileList[fileIndex];
    (<EditorSongFileComponent>fileDialog.content).song = songCopy;

    (<EditorSongFileComponent>fileDialog.content).onClose.subscribe(result => {
      if (result === 1) {
        // OK has been pressed -> save
        this.currentSong.fileList[fileIndex] = (<EditorSongFileComponent>fileDialog.content).file;

        this.rebuildFileListBasedOnType();
      }
    });
  }

  multipleVideoImage(): boolean {
    let videoImageCount: number = 0;

    if(!this.currentSong) {
      return false;
    }

    if(!this.currentSong.fileList) {
      return false;
    }

    for(let file of this.currentSong.fileList) {
      if(file.type == 'VIDEO' || file.type == 'IMAGE') {
        videoImageCount ++;

        if(videoImageCount > 1) {
          return true;
        }
      }
    }

    return false;
  }

}
