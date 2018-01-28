import { Subject } from 'rxjs/Subject';
import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent implements OnInit {

  onClose: Subject<void>;

  message: string;

  constructor(private bsModalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject<void>();
  }

  public ok(): void {
    this.onClose.next();
    this.bsModalRef.hide();
  }

}
