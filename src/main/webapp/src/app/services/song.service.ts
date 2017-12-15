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

  private mockSong: string = `{
      "name": "Dear Mr. Wise Guy",
      "autoStartNextSong": false,
      "notes": null,
      "durationMillis": 53000,
      "fileList": [
          {
              "midiFile": {
                  "name": "wise_guy.mid",
                  "active": true,
                  "durationMillis": 50791,
                  "offsetMillis": 1000,
                  "midiRoutingList": [
                      {
                          "midiDestination": "DMX",
                          "midiMapping": {
                              "channelMap": [],
                              "channelOffset": null,
                              "noteOffset": 10,
                              "overrideParent": false
                          },
                          "midi2DmxMapping": {
                              "mappingType": "SIMPLE"
                          },
                          "remoteDeviceId": []
                      },
                      {
                          "midiDestination": "OUT_DEVICE",
                          "midiMapping": {
                              "channelMap": [],
                              "channelOffset": null,
                              "noteOffset": 10,
                              "overrideParent": false
                          },
                          "midi2DmxMapping": {
                              "mappingType": "SIMPLE"
                          },
                          "remoteDeviceId": []
                      }
                  ]
              }
          },
          {
              "audioFile": {
                  "name": "wise_guy.wav",
                  "active": true,
                  "durationMillis": 53000,
                  "offsetMillis": 0,
                  "device": "stereo1"
              }
          }
      ]
  }`;

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
    if(environment.disconnected) {
      this.songs = [];
      this.songs.push(new Song(JSON.parse(this.mockSong)));
      return Observable.of(this.songs);
    }

    if (this.songs) {
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
    if(environment.disconnected) {
      return Observable.of(new Song(JSON.parse(this.mockSong)));
    }

    return this.apiService.get('song?name=' + songName)
      .map((response: Response) => {
        return new Song(response.json());
      });
  }

}
