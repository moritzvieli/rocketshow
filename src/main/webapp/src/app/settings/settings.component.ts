import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Settings } from '../models/settings';
import { SettingsService } from '../services/settings.service';
import { PendingChangesDialogService } from '../services/pending-changes-dialog.service';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

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
    private translateService: TranslateService,
    private toastrService: ToastrService) { }

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

  discard() {
    this.settingsService.getSettings(true).map(result => {
      this.copyInitialSettings(result);
      this.settingsService.settingsChanged.next();

      this.translateService.get(['settings.toast-discard-success', 'settings.toast-discard-success-title']).subscribe(result => {
        this.toastrService.success(result['settings.toast-discard-success'], result['settings.toast-discard-success-title']);
      });
    }).subscribe();
  }

  save() {
    this.settingsService.getSettings().map(result => {
      this.translateService.get('intro.default-unit-name').subscribe((defaultUnitName) => {

        if (!result.deviceName) {
          result.deviceName = defaultUnitName;
        }
        this.settingsService.saveSettings(result).subscribe();

        this.copyInitialSettings(result);
        this.translateService.use(result.language);

        this.settingsService.settingsChanged.next();

        this.translateService.get(['settings.toast-save-success', 'settings.toast-save-success-title']).subscribe(result => {
          this.toastrService.success(result['settings.toast-save-success'], result['settings.toast-save-success-title']);
        });
      });
    }).subscribe();
  }
}
