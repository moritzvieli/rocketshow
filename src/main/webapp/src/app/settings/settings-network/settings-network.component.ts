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
    private settingsServie: SettingsService
  ) { }

  ngOnInit() {
    this.settingsServie.getSettings().subscribe((response) => {
      this.settings = response;
    });
  }

  addRemoteDevice() {
    let remoteDevice: RemoteDevice = new RemoteDevice();

    this.settings.remoteDeviceList.push(remoteDevice);
  }

}
