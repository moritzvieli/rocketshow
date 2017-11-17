import { Component, OnChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('preventInitialChildAnimations', [
      transition(':enter', [
        query(':enter', [], {optional: true})
      ])
    ]),
    trigger('introAppEnters', [
      transition(':enter', [
        style({opacity: 0}),
        animate('200ms', style({opacity: 1}))
      ]),
    ]),
    trigger('introWizardLeaves', [
      transition(':leave', [
        style({opacity: 1, marginTop: 0}),
        animate('500ms', style({opacity: 0, marginTop: '-100px'}))
      ])
    ])
  ],
  host: {'[class.body-bg-moving]':'!showIntroApp'}
})
export class AppComponent {
  showIntroWizard = false;
  showIntroApp = true;
  files: any[] = [];

  constructor(private translate: TranslateService) {
    translate.setDefaultLang('en');

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

  switchLanguage(language: string) {
    this.translate.use(language);
  }

  finishIntroWizard() {
    // Hide the intro wizard
    this.showIntroWizard = false;

    // Show the app as soon as the intro wizard has been hidden
    setTimeout(() => {  
      this.showIntroApp = true;
    }, 500);
  }
}
