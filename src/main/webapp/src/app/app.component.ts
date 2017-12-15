import { StateService } from './services/state.service';
import { SongService } from './services/song.service';
import { Component, OnChanges, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { Router, NavigationEnd } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { environment } from '../environments/environment';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  host: { '[class.body-bg-moving]': 'this.isIntro' }
})
export class AppComponent implements OnInit {

  isIntro: boolean = false;
  loaded: boolean = false;

  constructor(
    private translate: TranslateService,
    private router: Router,
    private stateService: StateService,
    private songService: SongService) {

    translate.setDefaultLang('en');
  }

  ngOnInit() {
    this.router.events.subscribe((e) => {
      if (e instanceof NavigationEnd) {
        if (e.url == '/intro') {
          this.isIntro = true;
        } else {
          this.isIntro = false;
        }
      }
    });

    // Load some required data
    Observable.forkJoin(
      this.stateService.getState(),
      this.songService.getSetLists(),
      this.songService.getSongs(),
    ).subscribe(() => {
      this.loaded = true;
    });

    if(environment.disconnected) {
      this.loaded = true;
    }
  }

}
