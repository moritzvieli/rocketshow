import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Settings } from '../models/settings';
import { SettingsService } from '../services/settings.service';
import { PendingChangesDialogService } from '../services/pending-changes-dialog.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  // The settings as they were, when we loaded them
  initialSettings: Settings;

  constructor(
    private settingsService: SettingsService,
    private pendingChangesDialogService: PendingChangesDialogService,
    private translate: TranslateService,) { }

  ngOnInit() {
    this.settingsService.getSettings(true).map(result => {
      this.copyInitialSettings(result);
    }).subscribe();
  }

  private copyInitialSettings(settings: Settings) {
    this.initialSettings = JSON.parse(JSON.stringify(settings));
  }

  canDeactivate(): Observable<boolean> {
    return this.settingsService.getSettings().flatMap(result => {
      return this.pendingChangesDialogService.check(this.initialSettings, result, 'settings.warning-settings-changes');
    });
  }
  
  save() {
    this.settingsService.getSettings().map(result => {
      // TODO Save the settings
      this.copyInitialSettings(result);
      this.translate.use(result.language);
    }).subscribe();
  }
}
