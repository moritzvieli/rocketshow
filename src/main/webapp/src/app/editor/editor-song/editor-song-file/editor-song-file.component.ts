import { SongFile } from './../../../models/song-file';
import { MidiRouting } from './../../../models/midi-routing';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap';
import { Subject } from 'rxjs/Subject';
import { Song } from '../../../models/song';
import { RoutingDetailsComponent } from '../../../routing-details/routing-details.component';
import { SongMidiFile } from '../../../models/song-midi-file';
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
  onClose: Subject<boolean>;

  dropzoneConfig: DropzoneConfigInterface;

  constructor(
    private bsModalRef: BsModalRef,
    private modalService: BsModalService,
    private apiService: ApiService) {

    this.dropzoneConfig = {
      url: apiService.getRestUrl + "file/upload",
      addRemoveLinks: false,
      uploadMultiple: false,
      previewTemplate: '#dz-preview-template'
    };
  }

  ngOnInit() {
    this.onClose = new Subject();
  }

  public onOk(): void {
    this.onClose.next(true);
    this.bsModalRef.hide();
  }

  public onCancel(): void {
    this.onClose.next(false);
    this.bsModalRef.hide();
  }

  // Edit the routing details
  editRouting(midiRoutingIndex: number) {
    // Create a backup of the current song
    let songCopy: Song = new Song(JSON.parse(this.song.stringify()));

    // Show the routing details dialog
    // Keyboard = false, because the onClose will not be fired in this case
    let routingDialog = this.modalService.show(RoutingDetailsComponent, { keyboard: true, animated: true, backdrop: false, ignoreBackdropClick: true, class: "" });
    (<RoutingDetailsComponent>routingDialog.content).midiRouting = (<SongMidiFile>songCopy.fileList[this.fileIndex]).midiRoutingList[midiRoutingIndex];

    (<RoutingDetailsComponent>routingDialog.content).onClose.subscribe(result => {
      if (result === true) {
        // OK has been pressed -> save
        (<SongMidiFile>this.song.fileList[this.fileIndex]).midiRoutingList = (<SongMidiFile>songCopy.fileList[this.fileIndex]).midiRoutingList;
      }
    });
  }

  // Prevent the last item in the file-list to be draggable.
  // Taken from http://jsbin.com/tuyafe/1/edit?html,js,output
  sortMove(evt) {
    return evt.related.className.indexOf('no-sortjs') === -1;
  }

}
