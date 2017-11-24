import { Component, OnInit } from '@angular/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { ApiService, State } from '../services/api.service';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.scss']
})
export class PlayComponent implements OnInit {

  constructor(private apiService: ApiService) {
    apiService.state.subscribe((state: State) => {
      console.log(state);
    });
  }

  ngOnInit() {
  }

}
