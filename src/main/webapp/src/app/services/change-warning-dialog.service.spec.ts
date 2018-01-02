import { ChangeWarningDialogComponent } from './../change-warning-dialog/change-warning-dialog.component';
import { TestBed, inject } from '@angular/core/testing';

import { ChangeWarningDialogService } from './change-warning-dialog.service';

describe('ChangeWarningService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChangeWarningDialogComponent]
    });
  });

  it('should be created', inject([ChangeWarningDialogComponent], (service: ChangeWarningDialogComponent) => {
    expect(service).toBeTruthy();
  }));
});
