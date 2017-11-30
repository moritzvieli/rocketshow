import { Response } from '@angular/http';
import { Injectable } from '@angular/core';
import { $WebSocket, WebSocketSendMode, WebSocketConfig } from 'angular2-websocket/angular2-websocket';
import * as Rx from 'rxjs/Rx';
import { Observable, Subject } from 'rxjs/Rx';
import { environment } from '../../environments/environment';
import { ApiService } from './api.service';
import { State } from '../models/state';

@Injectable()
export class StateService {

  public state: Subject<State> = new Rx.Subject();
  private currentState: State;

  // The websocket endpoint url
  private wsUrl: string;

  // The websocket connection
  private websocket: $WebSocket;

  connected: boolean;

  constructor(private apiService: ApiService) {
    // Create the backend-url
    if (environment.name == 'dev') {
      this.wsUrl = 'ws://' + environment.localBackend + '/';
    } else {
      this.wsUrl = 'ws://' + location.hostname + '/';
    }

    this.wsUrl += 'state';

    // Connect to the websocket backend
    const wsConfig = { reconnectIfNotNormalClose: true } as WebSocketConfig;
    this.websocket = new $WebSocket(this.wsUrl, null, wsConfig);

    this.websocket.onMessage(
      (msg: MessageEvent) => {
        let state: State = new State(JSON.parse(msg.data));

        this.state.next(state);
        this.currentState = state;
      },
      { autoApply: false }
    );

    this.websocket.onOpen(() => {
      this.connected = true;
    });

    this.websocket.onClose(() => {
      this.connected = false;
    });
  }

  getState(): Observable<State> {
    if (this.currentState) {
      return Observable.of(this.currentState);
    }

    return this.apiService.get('system/state')
      .map((response: Response) => {
        this.currentState = new State(response.json());
        return this.currentState;
      });
  }

}
