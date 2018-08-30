import { LeadSheetService } from './../services/lead-sheet.service';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Rx';
import { State } from './../models/state';
import { StateService } from './../services/state.service';
import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-lead-sheet',
  templateUrl: './lead-sheet.component.html',
  styleUrls: ['./lead-sheet.component.scss']
})
export class LeadSheetComponent implements OnInit {

  @ViewChild('img') img: ElementRef;

  currentState: State = new State();
  imgStyle: any = {};
  playUpdateSubscription: Subscription;
  lastPlayTime: Date;

  constructor(
    public stateService: StateService,
    public leadSheetService: LeadSheetService
  ) { }

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
  }

  imgLoaded() {
    this.update();
  }

  update() {
    if(!this.leadSheetService.showLeadSheet) {
      return;
    }

    let imgHeight = this.img.nativeElement.height;
    let windowHeight = window.innerHeight - 16 * 2 /* Padding */;

    let positionMillis = this.currentState.positionMillis;
    
    if(this.lastPlayTime) {
      positionMillis = new Date().getTime() - this.lastPlayTime.getTime() + this.currentState.positionMillis;
    }
    
    let lengthMillis = this.currentState.currentCompositionDurationMillis;

    let scrollHeight = imgHeight - windowHeight;

    if(scrollHeight < 0 || lengthMillis == 0) {
      return;
    }

    let pos = scrollHeight * positionMillis / lengthMillis * -1;

    this.imgStyle = { 'margin-top': pos + 'px' };
  }

  private stateChanged(newState: State) {
    if (newState.playState == 'PLAYING' && this.currentState.playState != 'PLAYING') {
      if (this.playUpdateSubscription) {
        this.playUpdateSubscription.unsubscribe;
      }

      // Save the last time, we started the composition. Don't use device time, as it may be wrong.
      this.lastPlayTime = new Date();

      let playUpdater = Observable.timer(0, 10);
      this.playUpdateSubscription = playUpdater.subscribe(() => {
        this.update();
      });
    }

    if (newState.playState == 'STOPPING' || newState.playState == 'STOPPING' || newState.playState == 'PAUSED') {
      if (this.playUpdateSubscription) {
        this.playUpdateSubscription.unsubscribe();
      }
    }

    this.update();

    this.currentState = newState;
  }

  close() {
    this.leadSheetService.showLeadSheet = false;
  }

}
