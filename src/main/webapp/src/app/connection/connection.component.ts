import { Component, OnInit } from '@angular/core';
import { StateService } from './../services/state.service';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.scss']
})
export class ConnectionComponent implements OnInit {

  constructor(public stateService: StateService) { }

  ngOnInit() {
  }

}
