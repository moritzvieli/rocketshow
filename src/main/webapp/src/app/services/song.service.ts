import { Observable } from 'rxjs/Rx';
import { SetList } from './../models/setlist';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';

@Injectable()
export class SongService {

  private currentSetList: SetList;

  constructor(private apiService: ApiService) { }

  getCurrentSetList(): Observable<SetList> {
    if (this.currentSetList) {
      return Observable.of(this.currentSetList);
    }

    return this.apiService.get('setlist')
      .map((response: Response) => {
        this.currentSetList = new SetList(response.json());
        return this.currentSetList;
      });
  }

}
