import { SongService } from './../services/song.service';
import { StateService } from './../services/state.service';
import { SetList } from './../models/setlist';
import { Component, OnInit } from '@angular/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { State } from '../models/state';
import { ApiService } from '../services/api.service';
import { TransportService } from '../services/transport.service';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.scss']
})
export class PlayComponent implements OnInit {

  currentSetList: SetList;
  currentState: State = new State();

  setLists: SetList[];

  playPercentage: number = 0;
  playTime: string = '00:00.000';
  playUpdateSubscription: Subscription;

  manualSongSelection: boolean = false;

  totalPlayTime: string = '';

  constructor(public apiService: ApiService,
    private stateService: StateService,
    private songService: SongService,
    private transportService: TransportService) {
  }

  ngOnInit() {
    // Subscribe to the state-changed service
    this.stateService.state.subscribe((state: State) => {
      this.stateChanged(state);
    });

    // Load the current state
    this.stateService.getState().subscribe((state: State) => {
      this.stateChanged(state);
      this.currentState = state;
    });

    this.loadCurrentSetList();

    // Load all setlists
    this.songService.getSetLists().map(result => {
      this.setLists = result;
    })
    .subscribe();
  }

  private loadCurrentSetList() {
    // Load the current setlist
    this.songService.getCurrentSetList(true).subscribe((setList: SetList) => {
      if(setList) {
        this.currentSetList = setList;

        let totalDurationMillis: number = 0;

        for(let song of setList.songList) {
          totalDurationMillis += song.durationMillis;
        }

        this.totalPlayTime = this.msToTime(totalDurationMillis, false);
      }
    });
  }

  selectSetList(setList: SetList) {
    this.songService.loadSetList(setList.name).subscribe();
  } 

  private pad(num: number, size: number): string {
    if(!num) {
      num = 0;
    }

    let padded: string = num.toString();
    while (padded.length < size) {
      padded = '0' + padded;
    }

    return padded;
  }

  private msToTime(millis: number, includeMillis: boolean = true): string {
    let ms: number = Math.round(millis % 1000);
    let seconds: number = Math.floor(((millis % 360000) % 60000) / 1000);
    let minutes: number = Math.floor((millis % 3600000) / 60000);

    if(includeMillis) {
      return this.pad(minutes, 2) + ':' + this.pad(seconds, 2) + '.' + this.pad(ms, 3);
    } else {
      return this.pad(minutes, 2) + ':' + this.pad(seconds, 2);
    }
  }

  private stateChanged(newState: State) {
    if (newState.playState == 'PLAYING' && this.currentState.playState != 'PLAYING') {
      if (this.playUpdateSubscription) {
        this.playUpdateSubscription.unsubscribe;
      }

      let playUpdater = Observable.timer(0, 10);
      this.playUpdateSubscription = playUpdater.subscribe(() => {
        let currentTime = new Date();
        let passedMillis = currentTime.getTime() - this.currentState.lastStartTime.getTime();

        if (passedMillis > 0) {
          this.playTime = this.msToTime(passedMillis);
        }

        this.playPercentage = 100 * passedMillis / this.currentState.currentSongDurationMillis;
      });
    }

    if (newState.playState == 'STOPPED' && this.currentState.playState != 'STOPPED') {
      if (this.playUpdateSubscription) {
        this.playUpdateSubscription.unsubscribe();

      }

      this.playTime = '00:00.000';
      this.playPercentage = 0;
    }

    // Scroll the corresponding song into the view, except the user selected the
    // song here in the app.
    if (this.manualSongSelection) {
      // The next time, we receive a new song state, we should scroll into the view again
      this.manualSongSelection = false;
    } else {
      let songObject = document.querySelector('#song' + newState.currentSongIndex);
      if (songObject) {
        songObject.scrollIntoView();
      }
      let songSmallObject = document.querySelector('#songSmall' + newState.currentSongIndex);
      if (songObject) {
        songSmallObject.scrollIntoView();
      }
    }

    // The current setlist changed
    if (newState.currentSetListName != this.currentState.currentSetListName) {
      this.loadCurrentSetList();
    }

    this.currentState = newState;
  }

  play() {
    this.currentState.playState = 'LOADING';
    this.transportService.play().subscribe();
  }

  stop() {
    this.currentState.playState = 'STOPPING';
    this.transportService.stop().subscribe();
  }

  nextSong() {
    this.transportService.nextSong().subscribe();
  }

  previousSong() {
    this.transportService.previousSong().subscribe();
  }

  setSongIndex(index: number) {
    this.manualSongSelection = true;
    this.transportService.setSongIndex(index).subscribe();
  }

}
