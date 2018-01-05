import { Observable } from 'rxjs/Rx';
import { PendingChangesDialogService } from './../../services/pending-changes-dialog.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-editor-setlist',
  templateUrl: './editor-setlist.component.html',
  styleUrls: ['./editor-setlist.component.scss']
})
export class EditorSetlistComponent implements OnInit {

  constructor(private pendingChangesDialogService: PendingChangesDialogService) { }

  ngOnInit() {
  }

  checkPendingChanges(): Observable<boolean> {
    return Observable.of(true);

    // TODO
    //return this.pendingChangesDialogService.check(this.initialSong, this.currentSong, 'editor.warning-song-changes');
  }

}
