import { Settings } from './../../models/settings';
import { SettingsService } from './../../services/settings.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings-dmx',
  templateUrl: './settings-dmx.component.html',
  styleUrls: ['./settings-dmx.component.scss']
})
export class SettingsDmxComponent implements OnInit {

  settings: Settings;

  constructor(
    private settingsService: SettingsService,
  ) { }

  private loadSettings() {
    this.settingsService.getSettings().map(result => {
      this.settings = result;
    }).subscribe();
  }

  ngOnInit() {
    this.loadSettings();
  }

}
