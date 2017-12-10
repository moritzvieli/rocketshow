import { Component, OnInit } from '@angular/core';
import { trigger, state, animate, transition, style, query } from '@angular/animations';
import { Router } from '@angular/router';

@Component({
  selector: 'app-intro',
  templateUrl: './intro.component.html',
  styleUrls: ['./intro.component.scss'],
  animations: [
    trigger('wizardState', [
      state('inactive', style({
        opacity: 0,
        marginTop: "-100px"
      })),
      state('active',   style({
        opacity: 1,
        marginTop: 0
      })),
      transition('active => inactive', animate('500ms ease-out'))
    ])
  ]
})
export class IntroComponent implements OnInit {

  wizardState: String = 'active';

  constructor(private router: Router) { }

  ngOnInit() {
  }

  finish() {
    this.wizardState = 'inactive';

    // Show the app as soon as the intro wizard has been hidden
    setTimeout(() => {  
      this.router.navigate(['/play']);
    }, 500);
  }

}
