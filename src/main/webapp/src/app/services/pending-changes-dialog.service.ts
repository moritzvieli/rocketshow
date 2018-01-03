import { WarningDialogService } from './warning-dialog.service';
import { Observable } from 'rxjs/Observable';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class PendingChangesDialogService {

  constructor(
    private modalService: BsModalService,
    private translateService: TranslateService,
    private warningDialogService: WarningDialogService) { }

  // Checks the equality of two objects, shows a warning dialog if they don't match and
  // returns, whether the changes can be discarded
  check(objectOld: any, objectNew: any, message: string): Observable<boolean> {
    if(!objectOld) {
      // There is no old object (e.g. the user created a new one)
      return Observable.of(true);
    }

    if(JSON.stringify(objectOld) === JSON.stringify(objectNew)) {
      // The two objects match (no changes detected)
      return Observable.of(true);
    }

    return this.warningDialogService.show(message);
  }

}
