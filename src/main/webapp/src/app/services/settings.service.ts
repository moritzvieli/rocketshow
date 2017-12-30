import { Settings } from './../models/settings';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Response } from '@angular/http';
import { Language } from '../models/language';

@Injectable()
export class SettingsService {

  languages: Language[] = [];

  constructor(private apiService: ApiService) {
    let language: Language;

    language = new Language();
    language.key = 'en';
    language.name = 'English';
    this.languages.push(language);

    language = new Language();
    language.key = 'de';
    language.name = 'Deutsch';
    this.languages.push(language);
  }

  getSettings(): Observable<Settings> {
    return this.apiService.get('system/settings')
      .map((response: Response) => {
        return new Settings(response.json());
      });
  }

}
