import { Subject } from 'rxjs/Subject';
import { Song } from './../models/song';
import { Observable } from 'rxjs/Rx';
import { SetList } from './../models/setlist';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { environment } from '../../environments/environment';

@Injectable()
export class SongService {

  private songs: Song[];
  private setLists: SetList[];

  private currentSetList: SetList;

  // Fires, when songs have changed (new ones, deleted)
  songsChanged: Subject<void> = new Subject<void>();

  constructor(private apiService: ApiService) { }

  getCurrentSetList(clearCache: boolean = false): Observable<SetList> {
    if (this.currentSetList && !clearCache) {
      return Observable.of(this.currentSetList);
    }

    return this.apiService.get('setlist')
      .map((response: Response) => {
        this.currentSetList = new SetList(response.json());
        return this.currentSetList;
      });
  }

  getSongs(clearCache: boolean = false): Observable<Song[]> {
    if (this.songs && !clearCache) {
      return Observable.of(this.songs);
    }

    return this.apiService.get('song/list')
      .map((response: Response) => {
        this.songs = [];

        for (let song of response.json()) {
          this.songs.push(new Song(song));
        }

        return this.songs;
      });
  }

  getSetLists(clearCache: boolean = false): Observable<SetList[]> {
    if (this.setLists && !clearCache) {
      return Observable.of(this.setLists);
    }

    return this.apiService.get('setlist/list')
      .map((response: Response) => {
        this.setLists = [];

        for (let setList of response.json()) {
          this.setLists.push(new SetList(setList));
        }

        return this.setLists;
      });
  }

  getSong(songName: string): Observable<Song> {
    return this.apiService.get('song?name=' + songName)
      .map((response: Response) => {
        return new Song(response.json());
      });
  }

  // Load a setlist on the device
  loadSetList(name: string): Observable<Response> {
    return this.apiService.post('setlist/load?name=' + name, undefined);
  }

  // Get a setlist from the device
  getSetList(setListName: string): Observable<SetList> {
    return this.apiService.get('setlist/details?name=' + setListName)
      .map((response: Response) => {
        return new SetList(response.json());
      });
  }

  saveSong(song: Song): Observable<Response> {
    return this.apiService.post('song', song.stringify());
  }

  deleteSong(name: string): Observable<Response> {
    return this.apiService.post('song/delete?name=' + name, undefined);
  }

  saveSetList(setList: SetList): Observable<Response> {
    return this.apiService.post('setlist', setList.stringify());
  }

  deleteSetList(name: string): Observable<Response> {
    return this.apiService.post('setlist/delete?name=' + name, undefined);
  }

}
