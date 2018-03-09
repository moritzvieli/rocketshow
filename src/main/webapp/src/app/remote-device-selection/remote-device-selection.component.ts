import { SettingsService } from './../services/settings.service';
import { Settings } from './../models/settings';
import { RemoteDevice } from './../models/remote-device';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-remote-device-selection',
  templateUrl: './remote-device-selection.component.html',
  styleUrls: ['./remote-device-selection.component.scss']
})
export class RemoteDeviceSelectionComponent implements OnInit {
  // TODO Instead of any, an interface would be the proper way
  @Input() remoteDeviceListContainer: any;

  settings: Settings;

  constructor(
    private settingsService: SettingsService
  ) { }

  private loadSettings() {
    this.settingsService.getSettings().map(result => {
      this.settings = result;
    }).subscribe();
  }

  ngOnInit() {
    this.loadSettings();
  }

  remoteDeviceChecked(remoteDevice: RemoteDevice) {
    if(!this.remoteDeviceListContainer) {
      return;
    }

    if(!this.remoteDeviceListContainer.remoteDeviceList) {
      this.remoteDeviceListContainer.remoteDeviceList = [];
    }

    for (let midiControlRemoteDevice of this.remoteDeviceListContainer.remoteDeviceList) {
      if (midiControlRemoteDevice == remoteDevice.name) {
        return true;
      }
    }

    return false;
  }

  updateRemoteDeviceSync(remoteDevice: RemoteDevice, value: boolean) {
    if(!this.remoteDeviceListContainer) {
      return;
    }

    if(!this.remoteDeviceListContainer.remoteDeviceList) {
      this.remoteDeviceListContainer.remoteDeviceList = [];
    }

    if (value) {
      // Add the remote device, if not already added
      for (let existingRemoteDevice of this.remoteDeviceListContainer.remoteDeviceList) {
        if (existingRemoteDevice == remoteDevice.name) {
          // Already added -> nothing to do
          return;
        }
      }

      // Add the new remote device
      this.remoteDeviceListContainer.remoteDeviceList.push(remoteDevice.name);
    } else {
      // Remove the remote device, if not already removed
      for (let i = 0; i < this.remoteDeviceListContainer.remoteDeviceList.length; i++) {
        if (this.remoteDeviceListContainer.remoteDeviceList[i] == remoteDevice.name) {
          this.remoteDeviceListContainer.remoteDeviceList.splice(i, 1);
          return;
        }
      }
    }
  }

}
