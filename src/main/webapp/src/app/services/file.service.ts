import { Composition } from './../models/composition';
import { Observable } from 'rxjs/Rx';
import { Set } from './../models/set';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { environment } from '../../environments/environment';
import { CompositionFile } from '../models/composition-file';

@Injectable()
export class FileService {

  constructor(private apiService: ApiService) { }

  getFiles(): Observable<CompositionFile[]> {
    return this.apiService.get('file/list')
      .map((response: Response) => {
        let files: CompositionFile[] = [];

        for (let file of response.json()) {
          files.push(Composition.getFileObjectByType(file));
        }

        return files;
      });
  }

  deleteFile(file: CompositionFile): Observable<void> {
    return this.apiService.post('file/delete?name=' + file.name + '&type=' + file.type, undefined).map((response: Response) => {
      return null;
    });
  }

}
