import { Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';

import { State } from './../models/state';
import { Injectable } from '@angular/core';
import * as Rx from 'rxjs/Rx';
import { Observable, Subject } from 'rxjs/Rx';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiService extends Http {

  private stateSubject: Rx.Subject<MessageEvent>;
  public state: Subject<State>;

  // The websocket endpoint url
  private wsUrl: string;

  // The rest endpoint base url
  private restUrl: string;

  constructor(
    backend: XHRBackend,
    options: RequestOptions,
    private http: Http
  ) {
    super(backend, options);

    // Create the backend-urls
    if (environment.name == 'dev') {
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
        return new State(JSON.parse(response.data));
      });
  }

  getRestUrl(): string {
    return this.restUrl;
  }

  get(url: string): Observable<Response> {
    return super.get(this.restUrl + url);
  }

  post(url: string, body: any): Observable<Response> {
    return super.post(this.restUrl + url, body);
  }

  put(url: string, body: any): Observable<Response> {
    return super.put(this.restUrl + url, body);
  }

  delete(url: string): Observable<Response> {
    return super.delete(this.restUrl + url);
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
