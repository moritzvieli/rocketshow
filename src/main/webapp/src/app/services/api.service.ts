import { Injectable } from '@angular/core';
import * as Rx from 'rxjs/Rx';
import { Observable, Subject } from 'rxjs/Rx';

export interface State {
  playing: boolean
}

@Injectable()
export class ApiService {

  private stateSubject: Rx.Subject<MessageEvent>;
  public state: Subject<State>;

  constructor() {
    this.state = <Subject<State>>this.connect()
    .map((response: MessageEvent): State => {
        let data = JSON.parse(response.data);

        return {
            playing: data.playing
        }
    });
  }

  private createStateConnection(): Rx.Subject<MessageEvent> {
    let ws = new WebSocket('ws://localhost:8080/RocketShow/state');

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

  public connect(): Rx.Subject<MessageEvent> {
    if (!this.stateSubject) {
      this.stateSubject = this.createStateConnection();
    }
    return this.stateSubject;
  }

}
