import { TranslateService } from '@ngx-translate/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SongService } from './../../services/song.service';
import { SetList } from './../../models/setlist';
import { Observable } from 'rxjs/Rx';
import { PendingChangesDialogService } from './../../services/pending-changes-dialog.service';
import { Component, OnInit } from '@angular/core';
import { WarningDialogService } from '../../services/warning-dialog.service';
import { Song } from '../../models/song';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-editor-setlist',
  templateUrl: './editor-setlist.component.html',
  styleUrls: ['./editor-setlist.component.scss']
})
export class EditorSetlistComponent implements OnInit {

  searchName: string = '';

  loadingSetList: boolean = false;

  private setLists: SetList[];
  filteredSetLists: SetList[];
  currentSetList: SetList;

  filteredSongs: Song[];
  availableSongs: Song[];

  loadingSongs: boolean = false;
  loadingSetLists: boolean = false;

  // The setList, as it was when we loaded it
  initialSetList: SetList;

  constructor(
    private songService: SongService,
    private modalService: BsModalService,
    private warningDialogService: WarningDialogService,
    private pendingChangesDialogService: PendingChangesDialogService,
    private toastrService: ToastrService,
    private translateService: TranslateService) {
  }

  ngOnInit() {
    this.loadSetLists();
    this.loadAvailableSongs();

    // Subscribe to song changes
    this.songService.songsChanged.subscribe(() => {
      this.loadAvailableSongs();
    });
  }

  private loadAvailableSongs() {
    this.loadingSongs = true;

    this.songService.getSongs(true).subscribe((songs: Song[]) => {
      this.availableSongs = songs;
      this.filterSongs();

      this.loadingSongs = false;
    });
  }

  private loadSetLists() {
    this.loadingSetLists = true;

    this.songService.getSetLists(true).subscribe((setLists: SetList[]) => {
      this.setLists = setLists;
      this.filterSetLists();

      this.loadingSetLists = false;
    });
  }

  // Filter the setlist list
  filterSetLists(searchValue?: string) {
    if (!searchValue) {
      this.filteredSetLists = this.setLists;
      return;
    }

    this.filteredSetLists = [];

    for (let setList of this.setLists) {
      if (setList.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredSetLists.push(setList);
      }
    }
  }

  // Filter the available song list
  filterSongs(searchValue?: string) {
    if (!searchValue) {
      this.filteredSongs = this.availableSongs;
      return;
    }

    this.filteredSongs = [];

    for (let song of this.availableSongs) {
      if (song.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredSongs.push(song);
      }
    }
  }

  private copyInitialSetList() {
    let setListString = JSON.stringify(this.currentSetList);

    this.currentSetList = new SetList(JSON.parse(setListString));
    this.initialSetList = new SetList(JSON.parse(setListString));
  }

  checkPendingChanges(): Observable<boolean> {
    return this.pendingChangesDialogService.check(this.initialSetList, this.currentSetList, 'editor.warning-setlist-changes');
  }

  // Select a setList
  selectSetList(setList: SetList) {
    if (this.currentSetList && this.currentSetList.name == setList.name) {
      return;
    }

    this.checkPendingChanges().map(result => {
      if (result) {
        // Load the details of the selected setList
        this.loadingSetList = true;

        this.songService.getSetList(setList.name).subscribe((setList: SetList) => {
          this.currentSetList = setList;

          this.copyInitialSetList();
          this.loadingSetList = false;
        });
      }
    }).subscribe();
  }

  // Unselect a song
  unselect() {
    this.currentSetList = undefined;
    this.initialSetList = undefined;
  }

  // Create a new song
  createSetList() {
    this.currentSetList = new SetList();
    this.copyInitialSetList();
  }

  private saveApi(setList: SetList) {
    this.songService.saveSetList(setList).map(() => {
      this.loadSetLists();
      this.copyInitialSetList();

      this.translateService.get(['editor.toast-setlist-save-success', 'editor.toast-save-success-title']).subscribe(result => {
        this.toastrService.success(result['editor.toast-setlist-save-success'], result['editor.toast-save-success-title']);
      });
    }).subscribe();
  }

  // Save a new song
  save(setList: SetList) {
    this.currentSetList.name = this.currentSetList.name.replace(/\//g, '').replace(/\\/g, '');

    if(this.currentSetList.name.length < 1) {
      return;
    }

    // Delete the old song, if the name changed
    if (this.initialSetList && this.initialSetList.name && this.initialSetList.name != setList.name && this.initialSetList.name.length > 0) {
      this.songService.deleteSetList(this.initialSetList.name).map(() => {
        this.saveApi(setList);
      }).subscribe();
    } else {
      this.saveApi(setList);
    }
  }

  // Delete the setList
  delete(setList: SetList) {
    this.warningDialogService.show('editor.warning-delete-setlist').map(result => {
      if (result) {
        this.songService.deleteSetList(this.initialSetList.name).map(() => {
          this.unselect();
          this.loadSetLists();

          this.translateService.get(['editor.toast-setlist-delete-success', 'editor.toast-delete-success-title']).subscribe(result => {
            this.toastrService.success(result['editor.toast-setlist-delete-success'], result['editor.toast-delete-success-title']);
          });
        }).subscribe();
      }
    }).subscribe();
  }

  showAvailableSong(song: Song): boolean {
    if (this.currentSetList) {
      for (let setListSong of this.currentSetList.songList) {
        if (setListSong.name == song.name) {
          return false;
        }
      }
    }

    return true;
  }

  addSong(song: Song) {
    this.currentSetList.songList.push(song);
  }

  removeSong(song: Song) {
    for (var i = this.currentSetList.songList.length - 1; i >= 0; i--) {
      if (this.currentSetList.songList[i].name == song.name) {
        this.currentSetList.songList.splice(i, 1);
      }
    }
  }

  toggleAutoStartNextSong(song: Song) {
    song.autoStartNextSong = !song.autoStartNextSong;
  }

}
