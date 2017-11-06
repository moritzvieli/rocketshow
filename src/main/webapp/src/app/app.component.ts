import { Component, OnChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
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

  constructor(private translate: TranslateService) {
    translate.setDefaultLang('en');
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
