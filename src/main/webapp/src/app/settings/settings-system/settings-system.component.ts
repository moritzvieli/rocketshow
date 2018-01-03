import { WarningDialogService } from './../../services/warning-dialog.service';
import { Settings } from './../../models/settings';
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-settings-system',
  templateUrl: './settings-system.component.html',
  styleUrls: ['./settings-system.component.scss']
})
export class SettingsSystemComponent implements OnInit {

  // The settings as they were, when we loaded them
  initialSettings: Settings;
  
  settings: Settings;

  constructor(
    public settingsService: SettingsService,
    private translate: TranslateService,
    private warningDialogService: WarningDialogService,
    private apiService: ApiService) { }

  ngOnInit() {
    this.settingsService.getSettings().map(result => {
      this.settings = result;
    }).subscribe();
  }

  switchLanguage(language: string) {
    this.translate.use(language);
    this.settings.language = language;
  }

  reboot() {
    this.warningDialogService.show('settings.warning-reboot').map(result => {
      if(result) {
        this.apiService.post('system/reboot', undefined).subscribe();
      }
    }).subscribe();
  }

}
