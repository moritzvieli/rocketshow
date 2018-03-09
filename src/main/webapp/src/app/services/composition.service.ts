import { Subject } from 'rxjs/Subject';
import { Composition } from './../models/composition';
import { Observable } from 'rxjs/Rx';
import { Set } from './../models/set';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { environment } from '../../environments/environment';

@Injectable()
export class CompositionService {

  private compositions: Composition[];
  private sets: Set[];

  private currentSet: Set;

  // Fires, when compositions have changed (new ones, deleted)
  compositionsChanged: Subject<void> = new Subject<void>();

  constructor(private apiService: ApiService) { }

  getCurrentSet(clearCache: boolean = false): Observable<Set> {
    if (this.currentSet && !clearCache) {
      return Observable.of(this.currentSet);
    }

    return this.apiService.get('set')
      .map((response: Response) => {
        this.currentSet = new Set(response.json());
        return this.currentSet;
      });
  }

  getCompositions(clearCache: boolean = false): Observable<Composition[]> {
    if (this.compositions && !clearCache) {
      return Observable.of(this.compositions);
    }

    return this.apiService.get('composition/list')
      .map((response: Response) => {
        this.compositions = [];

        for (let composition of response.json()) {
          this.compositions.push(new Composition(composition));
        }

        return this.compositions;
      });
  }

  getSets(clearCache: boolean = false): Observable<Set[]> {
    if (this.sets && !clearCache) {
      return Observable.of(this.sets);
    }

    return this.apiService.get('set/list')
      .map((response: Response) => {
        this.sets = [];

        for (let set of response.json()) {
          this.sets.push(new Set(set));
        }

        return this.sets;
      });
  }

  getComposition(compositionName: string): Observable<Composition> {
    return this.apiService.get('composition?name=' + compositionName)
      .map((response: Response) => {
        return new Composition(response.json());
      });
  }

  // Load a set on the device
  loadSet(name: string): Observable<Response> {
    return this.apiService.post('set/load?name=' + name, undefined);
  }

  // Get a set from the device
  getSet(setName: string): Observable<Set> {
    return this.apiService.get('set/details?name=' + setName)
      .map((response: Response) => {
        return new Set(response.json());
      });
  }

  saveComposition(composition: Composition): Observable<Response> {
    return this.apiService.post('composition', composition.stringify());
  }

  deleteComposition(name: string): Observable<Response> {
    return this.apiService.post('composition/delete?name=' + name, undefined);
  }

  saveSet(set: Set): Observable<Response> {
    return this.apiService.post('set', set.stringify());
  }

  deleteSet(name: string): Observable<Response> {
    return this.apiService.post('set/delete?name=' + name, undefined);
  }

}
