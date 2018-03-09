import { Response } from '@angular/http';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TransportService {

  constructor(private apiService: ApiService) { }

  play(): Observable<null> {
    return this.apiService.post('transport/play', null).map((response: Response) => {
      return null;
    });
  }

  stop(): Observable<null> {
    return this.apiService.post('transport/stop', null).map((response: Response) => {
      return null;
    });
  }

  nextComposition(): Observable<null> {
    return this.apiService.post('transport/next-composition', null).map((response: Response) => {
      return null;
    });
  }

  previousComposition(): Observable<null> {
    return this.apiService.post('transport/previous-composition', null).map((response: Response) => {
      return null;
    });
  }

  setCompositionName(compositionName: string): Observable<null> {
    return this.apiService.post('transport/set-composition-name?name=' + compositionName, null).map((response: Response) => {
      return null;
    });
  }

  setCompositionIndex(index: number): Observable<null> {
    return this.apiService.post('transport/set-composition-index?index=' + index, null).map((response: Response) => {
      return null;
    });
  }

}
