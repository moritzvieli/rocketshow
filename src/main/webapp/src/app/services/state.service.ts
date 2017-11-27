import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { State } from '../models/state';

@Injectable()
export class StateService {

  private state: State;

  constructor(private apiService: ApiService) { }

  getState(): Observable<State> {
    if (this.state) {
      return Observable.of(this.state);
    }

    return this.apiService.get('system/state')
      .map((response: Response) => {
        this.state = new State(response.json();
        return this.state;
      });
  }

}
