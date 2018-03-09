import { ToastGeneralErrorService } from './../services/toast-general-error.service';
import { Component, OnInit, Directive, Output, EventEmitter } from '@angular/core';
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

  loading: boolean = true;

  constructor(
    private settingsService: SettingsService,
    private pendingChangesDialogService: PendingChangesDialogService,
    private translateService: TranslateService,
    private toastrService: ToastrService,
    private toastGeneralErrorService: ToastGeneralErrorService
  ) { }

  ngOnInit() {
    this.settingsService.getSettings(true).map(result => {
      this.copyInitialSettings(result);
      this.loading = false;
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
      this.translateService.get(['intro.default-unit-name', 'settings.remote-device-name-placeholder']).subscribe((translations) => {
        // Set some default settings
        if (!result.deviceName) {
          result.deviceName = translations['intro.default-unit-name'];
        }

        for (let i = 0; i < result.remoteDeviceList.length; i++) {
          let remoteDevice = result.remoteDeviceList[i];

          if (!remoteDevice.name) {
            remoteDevice.name = translations['settings.remote-device-name-placeholder'] + ' ' + (i + 1);
          }

          if (!remoteDevice.host) {
            remoteDevice.host = '192.168.1.22';
          }
        }

        // Save the settings on the device
        this.settingsService.saveSettings(result).map(() => {
          this.copyInitialSettings(result);
          this.translateService.use(result.language);

          this.settingsService.settingsChanged.next();

          this.translateService.get(['settings.toast-save-success', 'settings.toast-save-success-title']).subscribe(result => {
            this.toastrService.success(result['settings.toast-save-success'], result['settings.toast-save-success-title']);
          });
        })
          .catch((err) =>  {
            return this.toastGeneralErrorService.show(err);
          })
          .subscribe();
      });
    }).subscribe();
  }
  
}
