import { InfoDialogComponent } from './../info-dialog/info-dialog.component';
import { Observable } from 'rxjs/Observable';
import { TranslateService } from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal/bs-modal.service';

@Injectable()
export class InfoDialogService {

  constructor(
    private modalService: BsModalService,
    private translateService: TranslateService
  ) { }

  // Show an info dialog with an observable which resolves as soon as the user clicked ok
  show(message: string): Observable<void> {
    let fileDialog

    return this.translateService.get(message).map(result => {
      fileDialog = this.modalService.show(InfoDialogComponent, { keyboard: true, ignoreBackdropClick: true });
      (<InfoDialogComponent>fileDialog.content).message = result;
    }).flatMap(() => {
      return (<InfoDialogComponent>fileDialog.content).onClose;
    });
  }

}
