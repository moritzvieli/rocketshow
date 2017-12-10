import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';
import { SongFile } from '../../../models/song-file';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-editor-song-file',
  templateUrl: './editor-song-file.component.html',
  styleUrls: ['./editor-song-file.component.scss'],
  providers: [BsModalService]
})
export class EditorSongFileComponent implements OnInit {

  file: SongFile;
  onClose: Subject<boolean>;

  constructor(public bsModalRef: BsModalRef) { }

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

}
