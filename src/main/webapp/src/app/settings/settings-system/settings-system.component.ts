import { WarningDialogService } from './../../services/warning-dialog.service';
import { Settings } from './../../models/settings';
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { ApiService } from '../../services/api.service';
import { PendingChangesDialogService } from '../../services/pending-changes-dialog.service';

@Component({
  selector: 'app-settings-system',
  templateUrl: './settings-system.component.html',
  styleUrls: ['./settings-system.component.scss']
})
export class SettingsSystemComponent implements OnInit {
  
  settings: Settings;

  constructor(
    public settingsService: SettingsService,
    private warningDialogService: WarningDialogService,
    private apiService: ApiService) { }

  ngOnInit() {
    this.settingsService.getSettings(true).map(result => {
      this.settings = result;
    }).subscribe();
  }

  switchLanguage(language: string) {
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
