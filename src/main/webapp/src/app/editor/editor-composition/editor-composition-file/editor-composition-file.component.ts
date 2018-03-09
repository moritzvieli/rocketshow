import { WarningDialogService } from './../../../services/warning-dialog.service';
import { CompositionMidiFile } from './../../../models/composition-midi-file';
import { FileService } from './../../../services/file.service';
import { TranslateService } from '@ngx-translate/core';
import { CompositionFile } from './../../../models/composition-file';
import { MidiRouting } from './../../../models/midi-routing';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap';
import { Subject } from 'rxjs/Subject';
import { Composition } from '../../../models/composition';
import { RoutingDetailsComponent } from '../../../routing-details/routing-details.component';
import { DropzoneConfigInterface } from 'ngx-dropzone-wrapper/dist/lib/dropzone.interfaces';
import { ApiService } from '../../../services/api.service';

@Component({
  selector: 'app-editor-composition-file',
  templateUrl: './editor-composition-file.component.html',
  styleUrls: ['./editor-composition-file.component.scss'],
})
export class EditorCompositionFileComponent implements OnInit {

  fileIndex: number;
  file: CompositionFile;
  composition: Composition;

  onClose: Subject<number>;

  existingFiles: CompositionFile[] = [];
  filteredExistingFiles: CompositionFile[] = [];

  dropzoneConfig: DropzoneConfigInterface;
  uploadMessage: string;

  constructor(
    private bsModalRef: BsModalRef,
    private modalService: BsModalService,
    private apiService: ApiService,
    private translateService: TranslateService,
    private fileService: FileService,
    private warningDialogService: WarningDialogService) {

    this.dropzoneConfig = {
      url: apiService.getRestUrl() + 'file/upload',
      addRemoveLinks: false,
      maxFilesize: 10000 /* 10 GB */,
      acceptedFiles: 'audio/*,video/*',
      previewTemplate: `
      <div class="dz-preview dz-file-preview">
        <!-- The attachment details -->
        <div class="dz-details" style="text-align: left">
          <i class="fa fa-file-o"></i> <span data-dz-name></span> <small><span class="label label-default file-size" data-dz-size></span></small>
        </div>
        
        <!--div class="mt-5">
          <span data-dz-errormessage></span>
        </div-->
        
        <div class="progress mt-4 mb-1" style="height: 10px">
          <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width:0%;" data-dz-uploadprogress></div>
        </div>
      </div>
      `
    };

    translateService.get('editor.dropzone-message').map(result => {
      this.uploadMessage = '<h3 class="mb-0"><i class="fa fa-cloud-upload"></i></h3>' + result;
    }).subscribe();

    this.loadFiles();
  }

  private loadFiles() {
    this.fileService.getFiles().map(result => {
      this.existingFiles = result;
      this.filterExistingFiles();
    }).subscribe();
  }

  ngOnInit() {
    this.onClose = new Subject();
  }

  public ok(): void {
    this.onClose.next(1);
    this.bsModalRef.hide();
  }

  public cancel(): void {
    this.onClose.next(2);
    this.bsModalRef.hide();
  }

  // Edit the routing details
  editRouting(midiRoutingIndex: number, addNew: boolean = false) {
    // Create a backup of the current file
    let fileCopy: CompositionMidiFile = new CompositionMidiFile(JSON.parse(JSON.stringify(this.file)));

    if (addNew) {
      // Add a new routing, if necessary
      let newRouting: MidiRouting = new MidiRouting();
      newRouting.midiDestination = 'OUT_DEVICE';
      fileCopy.midiRoutingList.push(newRouting);
      midiRoutingIndex = fileCopy.midiRoutingList.length - 1;
    }

    // Show the routing details dialog
    let routingDialog = this.modalService.show(RoutingDetailsComponent, { keyboard: true, animated: true, backdrop: false, ignoreBackdropClick: true, class: "" });
    (<RoutingDetailsComponent>routingDialog.content).midiRouting = fileCopy.midiRoutingList[midiRoutingIndex];

    (<RoutingDetailsComponent>routingDialog.content).onClose.subscribe(result => {
      if (result === 1) {
        // OK has been pressed -> save
        (<CompositionMidiFile>this.file).midiRoutingList[midiRoutingIndex] = fileCopy.midiRoutingList[midiRoutingIndex];
      }
    });
  }

  // Prevent the last item in the file-list to be draggable.
  // Taken from http://jsbin.com/tuyafe/1/edit?html,js,output
  sortMove(evt) {
    return evt.related.className.indexOf('no-sortjs') === -1;
  }

  public onUploadError(args: any) {
    console.log('Upload error', args);
  }

  public onUploadSuccess(args: any) {
    this.loadFiles();

    // Hide the preview element
    args[0].previewElement.hidden = true;

    // Select this file
    let midiRoutingList;
    if (this.file && this.file.type == 'MIDI') {
      midiRoutingList = (<CompositionMidiFile>this.file).midiRoutingList;
    }

    this.file = Composition.getFileObjectByType(args[1]);

    if (this.file.type == 'MIDI' && midiRoutingList) {
      (<CompositionMidiFile>this.file).midiRoutingList = midiRoutingList;
    }
  }

  // Filter the existing files
  filterExistingFiles(searchValue?: string) {
    if (!searchValue) {
      this.filteredExistingFiles = this.existingFiles;
      return;
    }

    this.filteredExistingFiles = [];

    for (let file of this.existingFiles) {
      if (file.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredExistingFiles.push(file);
      }
    }
  }

  selectExistingFile(existingFile: CompositionFile) {
    if (this.file.name == existingFile.name && this.file.type == existingFile.type) {
      // This file is already selected
      return;
    }

    let midiRoutingList;
    if (this.file && this.file.type == 'MIDI') {
      midiRoutingList = (<CompositionMidiFile>this.file).midiRoutingList;
    }

    this.file = existingFile;

    if (this.file.type == 'MIDI' && midiRoutingList) {
      (<CompositionMidiFile>this.file).midiRoutingList = midiRoutingList;
    }
  }

  deleteRouting(midiRoutingIndex: number) {
    (<CompositionMidiFile>this.file).midiRoutingList.splice(midiRoutingIndex, 1);
  }

  deleteFile(existingFile: CompositionFile) {
    this.warningDialogService.show('editor.warning-delete-file').map(result => {
      if (result) {
        this.fileService.deleteFile(existingFile).subscribe(() => {
          this.loadFiles();
        });
      }
    }).subscribe();
  }

}
