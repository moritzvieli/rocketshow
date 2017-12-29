import { SongMidiFile } from './../../../models/song-midi-file';
import { FileService } from './../../../services/file.service';
import { TranslateService } from '@ngx-translate/core';
import { SongFile } from './../../../models/song-file';
import { MidiRouting } from './../../../models/midi-routing';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap';
import { Subject } from 'rxjs/Subject';
import { Song } from '../../../models/song';
import { RoutingDetailsComponent } from '../../../routing-details/routing-details.component';
import { DropzoneConfigInterface } from 'ngx-dropzone-wrapper/dist/lib/dropzone.interfaces';
import { ApiService } from '../../../services/api.service';

@Component({
  selector: 'app-editor-song-file',
  templateUrl: './editor-song-file.component.html',
  styleUrls: ['./editor-song-file.component.scss'],
})
export class EditorSongFileComponent implements OnInit {

  fileIndex: number;
  file: SongFile;
  song: Song;
  onClose: Subject<number>;

  existingFiles: SongFile[] = [];
  filteredExistingFiles: SongFile[] = [];

  dropzoneConfig: DropzoneConfigInterface;
  uploadMessage: string;

  constructor(
    private bsModalRef: BsModalRef,
    private modalService: BsModalService,
    private apiService: ApiService,
    private translateService: TranslateService,
    private fileService: FileService) {

    this.dropzoneConfig = {
      url: apiService.getRestUrl() + 'file/upload',
      addRemoveLinks: false,
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

    fileService.getFiles().map(result => {
      this.existingFiles = result;
      this.filterExistingFiles();
    }).subscribe();
  }

  ngOnInit() {
    this.onClose = new Subject();
  }

  public delete(): void {
    // TODO Show yes-no-dialog
    this.onClose.next(3);
    this.bsModalRef.hide();
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
    let fileCopy: SongMidiFile = new SongMidiFile(JSON.parse(JSON.stringify(this.file)));

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
        (<SongMidiFile>this.file).midiRoutingList[midiRoutingIndex] = fileCopy.midiRoutingList[midiRoutingIndex];
      } else if (result === 3) {
        // Delete has been pressed -> delete
        (<SongMidiFile>this.file).midiRoutingList.splice(midiRoutingIndex, 1);
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
    // Hide the preview element
    args[0].previewElement.hidden = true;

    // Select this file
    let midiRoutingList;
    if(this.file && this.file.type == 'MIDI') {
      midiRoutingList = (<SongMidiFile>this.file).midiRoutingList;
    }

    this.file = Song.getFileObjectByType(args[1]);

    if(this.file.type == 'MIDI' && midiRoutingList) {
      (<SongMidiFile>this.file).midiRoutingList = midiRoutingList;
    }
  }

  // Filter the existing files
  filterExistingFiles(searchValue?: string) {
    if (!searchValue) {
      this.filteredExistingFiles = this.existingFiles;
      return;
    }

    this.filteredExistingFiles = [];

    for (let song of this.existingFiles) {
      if (song.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredExistingFiles.push(song);
      }
    }
  }

  selectExistingFile(existingFile: SongFile) {
    if(this.file.name == existingFile.name && this.file.type == existingFile.type) {
      // This file is already selected
      return;
    }

    let midiRoutingList;
    if(this.file && this.file.type == 'MIDI') {
      midiRoutingList = (<SongMidiFile>this.file).midiRoutingList;
    }

    this.file = existingFile;

    if(this.file.type == 'MIDI' && midiRoutingList) {
      (<SongMidiFile>this.file).midiRoutingList = midiRoutingList;
    }
  }

}
