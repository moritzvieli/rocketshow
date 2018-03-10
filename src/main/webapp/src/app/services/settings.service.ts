import { AudioDevice } from './../models/audio-device';
import { MidiDevice } from './../models/midi-device';
import { Subject } from 'rxjs/Rx';
import { Settings } from './../models/settings';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { Language } from '../models/language';

@Injectable()
export class SettingsService {

  // Fires, when the settings have changed
  settingsChanged: Subject<void> = new Subject<void>();

  languages: Language[] = [];
  settings: Settings;
  observable: Observable<Settings>;

  constructor(private apiService: ApiService) {
    let language: Language;

    language = new Language();
    language.key = 'en';
    language.name = 'English';
    this.languages.push(language);

    language = new Language();
    language.key = 'de';
    language.name = 'Deutsch';
    this.languages.push(language);
  }

  getSettings(clearCache: boolean = false): Observable<Settings> {
    if(clearCache) {
      this.settings = undefined;
      this.observable = undefined;
    }

    if (this.settings) {
      return Observable.of(this.settings);
    }

    if(this.observable) {
      return this.observable;
    }

    this.observable = this.apiService.get('system/settings')
    .map((response: Response) => {
      if(!this.settings) {
        this.settings = new Settings(response.json());
      }
      this.observable = undefined;
      
      return this.settings;
    });

    return this.observable;
  }

  saveSettings(settings: Settings): Observable<Response> {
    return this.apiService.post('system/settings', JSON.stringify(settings));
  }

  private apiGetMidiDevices(url: string) {
    return this.apiService.get('midi/' + url)
    .map((response: Response) => {
      let deviceList: MidiDevice[] = [];

      for (let midiDevice of response.json()) {
        deviceList.push(new MidiDevice(midiDevice));
      }

      return deviceList;
    });
  }

  getMidiInDevices(): Observable<MidiDevice[]> {
    return this.apiGetMidiDevices('in-devices');
  }

  getMidiOutDevices(): Observable<MidiDevice[]> {
    return this.apiGetMidiDevices('out-devices');
  }

  getAudioDevices(): Observable<AudioDevice[]> {
    return this.apiService.get('audio/devices')
    .map((response: Response) => {
      let deviceList: AudioDevice[] = [];

      for (let audioDevice of response.json()) {
        deviceList.push(new AudioDevice(audioDevice));
      }

      return deviceList;
    });
  }

}
