import { Http, XHRBackend, RequestOptions, Request, RequestOptionsArgs, Response, Headers } from '@angular/http';

import { State } from './../models/state';
import { Injectable } from '@angular/core';
import * as Rx from 'rxjs/Rx';
import { Observable, Subject } from 'rxjs/Rx';
import { environment } from '../../environments/environment';
import { $WebSocket, WebSocketSendMode, WebSocketConfig } from 'angular2-websocket/angular2-websocket';

@Injectable()
export class ApiService extends Http {

  public state: Subject<State> = new Rx.Subject();

  // The websocket endpoint url
  private wsUrl: string;

  // The rest endpoint base url
  private restUrl: string;

  // The websocket connection
  private websocket: $WebSocket;

  connected: boolean;

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
    const wsConfig = { reconnectIfNotNormalClose: true } as WebSocketConfig;
    this.websocket = new $WebSocket(this.wsUrl, null, wsConfig);

    this.websocket.onMessage(
      (msg: MessageEvent) => {
        this.state.next(new State(JSON.parse(msg.data)));
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

}
