import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Version } from '../models/version';

@Injectable()
export class UpdateService {

  constructor(private apiService: ApiService) { }

  // Get the version of the device
  getCurrentVersion(): Observable<Version> {
    return this.apiService.get('system/current-version')
    .map(result => {
      return new Version(result.json());
    });
  }

  // Get the latest available version
  getRemoteVersion(): Observable<Version> {
    return this.apiService.get('system/remote-version')
    .map(result => {
      return new Version(result.json());
    });
  }

  doUpdate(): Observable<null> {
    return this.apiService.post('system/update', null).map(() => {
      return null;
    });
  }

}
