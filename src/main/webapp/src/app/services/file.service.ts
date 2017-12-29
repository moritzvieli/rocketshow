import { Song } from './../models/song';
import { Observable } from 'rxjs/Rx';
import { SetList } from './../models/setlist';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { environment } from '../../environments/environment';
import { SongFile } from '../models/song-file';

@Injectable()
export class FileService {

  constructor(private apiService: ApiService) { }

  getFiles(): Observable<SongFile[]> {
    return this.apiService.get('file/list')
      .map((response: Response) => {
        let files: SongFile[] = [];

        for (let file of response.json()) {
          files.push(new SongFile(file));
        }

        return files;
      });
  }

}
