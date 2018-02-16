import { Response, ResponseContentType } from '@angular/http';
import { InfoDialogService } from './../../services/info-dialog.service';
import { WaitDialogService } from './../../services/wait-dialog.service';
import { StateService } from './../../services/state.service';
import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../services/settings.service';
import { Settings } from '../../models/settings';
import { WarningDialogService } from '../../services/warning-dialog.service';
import { ApiService } from '../../services/api.service';
import { State } from '../../models/state';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-settings-advanced',
  templateUrl: './settings-advanced.component.html',
  styleUrls: ['./settings-advanced.component.scss']
})
export class SettingsAdvancedComponent implements OnInit {

  settings: Settings;
  private isResettingToFactory: boolean = false;

  constructor(
    private settingsService: SettingsService,
    private warningDialogService: WarningDialogService,
    private waitDialogService: WaitDialogService,
    private apiService: ApiService,
    private stateService: StateService,
    private infoDialogService: InfoDialogService) { }

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

    this.stateService.state.subscribe((state: State) => {
      if(this.isResettingToFactory) {
        // We got a new state after resetting to factory defaults
        // -> the device has been resetted
        this.isResettingToFactory = false;

        this.infoDialogService.show('settings.factory-reset-done').map(() => {
          location.reload();
        }).subscribe();
      }
    });
  }

  factoryReset() {
    this.warningDialogService.show('settings.warning-factory-reset').map(result => {
      if (result) {
        this.waitDialogService.show('settings.wait-factory-reset');
        this.isResettingToFactory = true;
        this.apiService.post('system/factory-reset', undefined).subscribe();
      }
    }).subscribe();
  }

  private downloadFile(response: Response){
    saveAs(response.blob(), 'logs.zip');
  }

  downloadLogs() {
    this.apiService.get('system/download-logs', {responseType: ResponseContentType.Blob}).subscribe(response => {
      this.downloadFile(response);
    });
  }

}
