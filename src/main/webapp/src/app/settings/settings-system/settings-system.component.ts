import { Settings } from './../../models/settings';
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-settings-system',
  templateUrl: './settings-system.component.html',
  styleUrls: ['./settings-system.component.scss']
})
export class SettingsSystemComponent implements OnInit {

  settings: Settings;

  constructor(
    public settingsService: SettingsService,
    private translate: TranslateService) { }

  ngOnInit() {
    this.settingsService.getSettings().map(result => {
      this.settings = result;
    }).subscribe();
  }

  switchLanguage(language: string) {
    this.translate.use(language);
    this.settings.language = language;
  }

}
