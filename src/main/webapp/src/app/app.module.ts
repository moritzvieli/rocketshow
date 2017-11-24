import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SortablejsModule } from 'angular-sortablejs';

import { AlertModule } from 'ngx-bootstrap';

import { AppComponent } from './app.component';
import { IntroComponent } from './intro/intro.component';
import { PlayComponent } from './play/play.component';
import { SettingsComponent } from './settings/settings.component';
import { EditorComponent } from './editor/editor.component';
import { EditorSongComponent } from './editor/editor-song/editor-song.component';
import { EditorSetlistComponent } from './editor/editor-setlist/editor-setlist.component';

import { ApiService } from './services/api.service';

const appRoutes: Routes = [
  { path: 'intro', component: IntroComponent },
  { path: 'play', component: PlayComponent },
  { path: 'editor', component: EditorComponent },
  { path: 'settings', component: SettingsComponent },
  { path: '',
    redirectTo: '/play',
    pathMatch: 'full'
  },
  /*{ path: '**', component: PlayComponent }*/
];

@NgModule({
  declarations: [
    AppComponent,
    IntroComponent,
    PlayComponent,
    SettingsComponent,
    EditorComponent,
    EditorSongComponent,
    EditorSetlistComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    ),
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    SortablejsModule.forRoot({ 
      animation: 300,
      handle: '.list-sort-handle'
    }),
    AlertModule.forRoot()
  ],
  providers: [
    ApiService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}