import { Observable } from 'rxjs/Observable';
import { TranslateService } from '@ngx-translate/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Injectable } from '@angular/core';
import { WaitDialogComponent } from '../wait-dialog/wait-dialog.component';

@Injectable()
export class WaitDialogService {

  constructor(
    private modalService: BsModalService,
    private translateService: TranslateService
  ) { }

  // Show a wait dialog
  show(message: string): void {
    this.translateService.get(message).map(result => {
      let fileDialog = this.modalService.show(WaitDialogComponent, { keyboard: false, ignoreBackdropClick: true });
      (<WaitDialogComponent>fileDialog.content).message = result;
    }).subscribe();
  }

}
