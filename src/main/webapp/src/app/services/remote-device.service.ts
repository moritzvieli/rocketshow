import { Observable } from 'rxjs/Rx';
import { RemoteDevice } from './../models/remote-device';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { ApiService } from './api.service';
import { Response } from '@angular/http';

@Injectable()
export class RemoteDeviceService {

  remoteDeviceList: RemoteDevice[];

  constructor(private apiService: ApiService) { }

  getRemoteDevices(): Observable<RemoteDevice[]> {
    if (this.remoteDeviceList) {
      return Observable.of(this.remoteDeviceList);
    }

    return this.apiService.get('system/remote-device')
      .map((response: Response) => {
        this.remoteDeviceList = [];

        for (let song of response.json()) {
          this.remoteDeviceList.push(new RemoteDevice(song));
        }

        return this.remoteDeviceList;
      });
  }

}
