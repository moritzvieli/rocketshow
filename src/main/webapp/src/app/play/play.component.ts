import { SetList } from './../models/setlist';
import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { State } from '../models/state';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.scss']
})
export class PlayComponent implements OnInit {

  currentSetList: SetList;
  currentState: State = new State();

  constructor(private apiService: ApiService) {
  }

  ngOnInit() {
    // Subscribe to the state-changed service
    this.apiService.state.subscribe((state: State) => {
      this.currentState = state;
      console.log(state);
    });

    // Load the current state
    this.apiService.get('system/state').map((response: Response) => {
      this.currentState = new State(response.json());
    }).subscribe();

    // Load the current setlist
    this.apiService.get('setlist').map((response: Response) => {
      this.currentSetList = new SetList(response.json());
    }).subscribe();
  }

  play() {
    this.currentState.playState = 'LOADING';

    this.apiService.post('transport/play', null).map((response: Response) => {
    }).subscribe();
  }

  stop() {
    this.currentState.playState = 'STOPPED';

    this.apiService.post('transport/stop', null).map((response: Response) => {
    }).subscribe();
  }

}
