import { TranslateService } from '@ngx-translate/core';
import { RemoteDevice } from './../../models/remote-device';
import { Settings } from './../../models/settings';
import { SettingsService } from './../../services/settings.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings-network',
  templateUrl: './settings-network.component.html',
  styleUrls: ['./settings-network.component.scss']
})
export class SettingsNetworkComponent implements OnInit {

  settings: Settings;

  constructor(
    private settingsService: SettingsService,
    private translateService: TranslateService
  ) { }

  private loadSettings() {
    this.settingsService.getSettings().map(result => {
      this.settings = result;
    }).subscribe();
  }

  ngOnInit() {
    this.loadSettings();

    this.settingsService.settingsChanged.subscribe(() => {
      this.loadSettings();
    });
  }

  addRemoteDevice() {
    this.translateService.get('settings.remote-device-name-placeholder').subscribe(result => {
      let remoteDevice: RemoteDevice = new RemoteDevice();
      remoteDevice.name = result + ' ' + (this.settings.remoteDeviceList.length + 1);
      this.settings.remoteDeviceList.push(remoteDevice);
    });
  }

  deleteRemoteDevice(remoteDeviceIndex: number) {
    this.settings.remoteDeviceList.splice(remoteDeviceIndex, 1);
  }

}
