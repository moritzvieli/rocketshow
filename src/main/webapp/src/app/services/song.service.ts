import { Song } from './../models/song';
import { Observable } from 'rxjs/Rx';
import { SetList } from './../models/setlist';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';

@Injectable()
export class SongService {

  private songs: Song[];
  private setLists: SetList[];

  private currentSetList: SetList;

  constructor(private apiService: ApiService) { }

  getCurrentSetList(): Observable<SetList> {
    if (this.currentSetList) {
      return Observable.of(this.currentSetList);
    }

    return this.apiService.get('setlist')
      .map((response: Response) => {
        this.currentSetList = new SetList(response.json());
        return this.currentSetList;
      });
  }

  getSongs(): Observable<Song[]> {
    if (this.currentSetList) {
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

  getSetLists(): Observable<SetList[]> {
    if (this.setLists) {
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

  loadSong(songName: string): Observable<Song> {
    return this.apiService.get('song?name=' + songName)
      .map((response: Response) => {
        return new Song(response.json());
      });
  }

}
