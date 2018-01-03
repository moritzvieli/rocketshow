import { PendingChangesDialogService } from './pending-changes-dialog.service';

import { TestBed, inject } from '@angular/core/testing';

describe('ChangeWarningService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PendingChangesDialogService]
    });
  });

  it('should be created', inject([PendingChangesDialogService], (service: PendingChangesDialogService) => {
    expect(service).toBeTruthy();
  }));
});
