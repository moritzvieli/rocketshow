import { Injectable } from '@angular/core';
import * as Rx from 'rxjs/Rx';
import { Observable, Subject } from 'rxjs/Rx';
import { environment } from '../../environments/environment';

export interface State {
  playing: boolean
}

@Injectable()
export class ApiService {

  private stateSubject: Rx.Subject<MessageEvent>;
  public state: Subject<State>;

  // The websocket endpoint url
  private wsUrl: string;

  // The rest endpoint base url
  private restUrl: string;

  constructor() {
    // Create the backend-urls
    if(environment.name == 'dev') {
      this.restUrl = 'http://' + environment.localBackend + '/';
      this.wsUrl = 'ws://' + environment.localBackend + '/';
    } else {
      this.restUrl = '/'
      this.wsUrl = 'ws://' + location.hostname + '/';
    }

    this.restUrl += 'api/';
    this.wsUrl += 'state';

    // Connect to the websocket backend
    this.state = <Subject<State>>this.connectStateConnection()
      .map((response: MessageEvent): State => {
        let data = JSON.parse(response.data);

        return {
          playing: data.playing
        }
      });
  }

  private createStateConnection(): Rx.Subject<MessageEvent> {
    let ws = new WebSocket(this.wsUrl);

    let observable = Rx.Observable.create(
      (obs: Rx.Observer<MessageEvent>) => {
        ws.onmessage = obs.next.bind(obs);
        ws.onerror = obs.error.bind(obs);
        ws.onclose = obs.complete.bind(obs);
        return ws.close.bind(ws);
      })

    let observer = {
      next: (data: Object) => {
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify(data));
        }
      }
    }

    return Rx.Subject.create(observer, observable);
  }

  public connectStateConnection(): Rx.Subject<MessageEvent> {
    if (!this.stateSubject) {
      this.stateSubject = this.createStateConnection();
    }
    return this.stateSubject;
  }

}
