import { EditorSongComponent } from './editor-song/editor-song.component';
import { Observable } from 'rxjs/Observable';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ComponentCanDeactivate } from '../pending-changes.guard';
import { EditorSetlistComponent } from './editor-setlist/editor-setlist.component';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.scss']
})
export class EditorComponent implements OnInit, ComponentCanDeactivate {

  @ViewChild(EditorSongComponent) editorSongComponent: EditorSongComponent;
  @ViewChild(EditorSetlistComponent) editorSetlistComponent: EditorSetlistComponent;
  
  constructor() {
  }

  ngOnInit() {
  }

  canDeactivate(): Observable<boolean> {
    return this.editorSongComponent.checkPendingChanges()
    .flatMap(result => {
      return result && this.editorSetlistComponent.checkPendingChanges();
    });
  }

}
