import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Session } from './../models/session';
import { Response } from '@angular/http';

@Injectable()
export class SessionService {

  session: Session;
  observable: Observable<Session>;

  constructor(private apiService: ApiService) { }

  getSession(clearCache: boolean = false): Observable<Session> {
    if(clearCache) {
      this.session = undefined;
      this.observable = undefined;
    }

    if (this.session) {
      return Observable.of(this.session);
    }

    if(this.observable) {
      return this.observable;
    }

    this.observable = this.apiService.get('session')
    .map(result => {
      this.session = new Session(result.json());
      this.observable = undefined;

      return this.session;
    });

    return this.observable;
  }

  introFinished(): Observable<Response> {
    return this.apiService.post('session/wizard-finished', undefined);
  }

}
