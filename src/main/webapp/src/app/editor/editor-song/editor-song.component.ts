import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-editor-song',
  templateUrl: './editor-song.component.html',
  styleUrls: ['./editor-song.component.scss']
})
export class EditorSongComponent implements OnInit {

  files: any[] = [];

  constructor() {
    var file1: any = {};
    file1.name = 'wise_guy.mid';
    file1.type = 'midi';
    this.files.push(file1);

    var file2: any = {};
    file2.name = 'wise_guy_click.wav';
    file2.type = 'audio';
    this.files.push(file2);

    var file3: any = {};
    file3.name = 'wise_guy.mp4';
    file3.type = 'video';
    this.files.push(file3);
  }

  ngOnInit() {
  }

  sortMove(evt) {
    // Taken from http://jsbin.com/tuyafe/1/edit?html,js,output
    return evt.related.className.indexOf('no-sortjs') === -1;
  }

}
