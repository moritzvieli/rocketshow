import { BsModalRef } from 'ngx-bootstrap/modal';
import { Composition } from './../../../models/composition';
import { Subject } from 'rxjs';
import { map } from "rxjs/operators";
import { Component, OnInit } from '@angular/core';
import { LeadSheet } from '../../../models/lead-sheet';
import { WarningDialogService } from '../../../services/warning-dialog.service';
import { DiskSpaceService } from '../../../services/disk-space.service';
import { LeadSheetService } from '../../../services/lead-sheet.service';
import { Settings } from '../../../models/settings';
import { SettingsService } from '../../../services/settings.service';

@Component({
  selector: 'app-editor-composition-lead-sheet',
  templateUrl: './editor-composition-lead-sheet.component.html',
  styleUrls: ['./editor-composition-lead-sheet.component.scss']
})
export class EditorCompositionLeadSheetComponent implements OnInit {

  selectUndefinedOptionValue: any = undefined;

  leadSheetIndex: number;
  leadSheet: LeadSheet;
  settings: Settings;

  onClose: Subject<number>;
  composition: Composition;

  existingLeadSheets: LeadSheet[] = [];
  filteredExistingLeadSheets: LeadSheet[] = [];

  diskSpaceUsedGB: number = 0;
  diskSpaceAvailableGB: number = 0;
  diskSpacePercentage: number = 0;

  constructor(
    private bsModalRef: BsModalRef,
    private warningDialogService: WarningDialogService,
    private diskSpaceService: DiskSpaceService,
    private leadSheetService: LeadSheetService,
    private settingsService: SettingsService
  ) {
    this.loadLeadSheets();
    this.loadDiskSpace();
  }

  private loadSettings() {
    this.settingsService.getSettings().pipe(map(result => {
      this.settings = result;
    })).subscribe();
  }

  private loadLeadSheets() {
    this.leadSheetService.getLeadSheets().pipe(map(result => {
      this.existingLeadSheets = result;
      this.filterExistingLeadSheets();
    })).subscribe();
  }

  ngOnInit() {
    this.onClose = new Subject();

    this.loadSettings();

    this.settingsService.settingsChanged.subscribe(() => {
      this.loadSettings();
    });
  }

  private loadDiskSpace() {
    this.diskSpaceService.getDiskSpace().pipe(map(diskSpace => {
      this.diskSpaceUsedGB = Math.round(diskSpace.usedMB / 10) / 100;
      this.diskSpaceAvailableGB = Math.round(diskSpace.availableMB / 10) / 100;

      if(diskSpace.usedMB != 0) {
        this.diskSpacePercentage = Math.round(diskSpace.availableMB / diskSpace.usedMB);
      }
    })).subscribe();
  }

  public onUploadError(args: any) {
    console.log('Upload error', args);
  }

  public onUploadSuccess(args: any) {
    this.loadLeadSheets();

    // Hide the preview element
    args[0].previewElement.hidden = true;

    // Select this file
    let instrumentUuuid;
    if (this.leadSheet) {
      instrumentUuuid = this.leadSheet.instrumentUuid;
    }

    this.leadSheet = new LeadSheet(args[1]);
    this.leadSheet.instrumentUuid = instrumentUuuid;
  }

  public ok(): void {
    this.onClose.next(1);
    this.bsModalRef.hide();
  }

  public cancel(): void {
    this.onClose.next(2);
    this.bsModalRef.hide();
  }

  // Filter the existing lead sheets
  filterExistingLeadSheets(searchValue?: string) {
    if (!searchValue) {
      this.filteredExistingLeadSheets = this.existingLeadSheets;
      return;
    }

    this.filteredExistingLeadSheets = [];

    for (let leadSheet of this.existingLeadSheets) {
      if (leadSheet.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredExistingLeadSheets.push(leadSheet);
      }
    }
  }

  selectExistingLeadSheet(existingLeadSheet: LeadSheet) {
    if (this.leadSheet && this.leadSheet.name == existingLeadSheet.name) {
      // This lead sheet is already selected
      return;
    }

    this.leadSheet = existingLeadSheet;
  }

  deleteLeadSheet(existingLeadSheet: LeadSheet) {
    this.warningDialogService.show('editor.warning-delete-file').pipe(map(result => {
      if (result) {
        this.leadSheetService.deleteLeadSheet(existingLeadSheet).subscribe(() => {
          this.loadLeadSheets();
          this.loadDiskSpace();
        });
      }
    })).subscribe();
  }

}
