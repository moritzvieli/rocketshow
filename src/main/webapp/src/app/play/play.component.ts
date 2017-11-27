import { SongService } from './../services/song.service';
import { StateService } from './../services/state.service';
import { SetList } from './../models/setlist';
import { Component, OnInit } from '@angular/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { State } from '../models/state';
import { ApiService } from '../services/api.service';
import { TransportService } from '../services/transport.service';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.scss']
})
export class PlayComponent implements OnInit {

  currentSetList: SetList;
  currentState: State = new State();

  constructor(public apiService: ApiService,
    private stateService: StateService,
    private songService: SongService,
    private transportService: TransportService) {
  }

  ngOnInit() {
    // Subscribe to the state-changed service
    this.apiService.state.subscribe((state: State) => {
      this.currentState = state;
    });

    // Load the current state
    this.stateService.getState().subscribe((state: State) => {
      this.currentState = state;
    });

    // Load the current setlist
    this.songService.getCurrentSetList().subscribe((setList: SetList) => {
      this.currentSetList = setList;
    });
  }

  play() {
    this.currentState.playState = 'LOADING';
    this.transportService.play().subscribe();
  }

  stop() {
    this.currentState.playState = 'STOPPING';
    this.transportService.stop().subscribe();
  }

}
