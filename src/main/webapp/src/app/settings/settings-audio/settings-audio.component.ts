import { AudioDevice } from './../../models/audio-device';
import { AudioBus } from './../../models/audio-bus';
import { Settings } from './../../models/settings';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from './../../services/settings.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings-audio',
  templateUrl: './settings-audio.component.html',
  styleUrls: ['./settings-audio.component.scss']
})
export class SettingsAudioComponent implements OnInit {

  settings: Settings;
  audioDeviceList: AudioDevice[];
  audioOutputList: string[] = [];

  constructor(
    private settingsService: SettingsService,
    private translateService: TranslateService
  ) {
    this.audioOutputList.push('HEADPHONES');
    this.audioOutputList.push('HDMI');
    this.audioOutputList.push('DEVICE');
  }

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

    this.settingsService.getAudioDevices().subscribe((response) => {
      this.audioDeviceList = response;
    });
  }

  addAudioBus() {
    this.translateService.get('settings.audio-bus-name-placeholder').subscribe(result => {
      let audioBus: AudioBus = new AudioBus();
      audioBus.name = result + ' ' + (this.settings.audioBusList.length + 1);
      this.settings.audioBusList.push(audioBus);
    });
  }

  deleteAudioBus(audioBusIndex: number) {
    this.settings.audioBusList.splice(audioBusIndex, 1);
  }

}
