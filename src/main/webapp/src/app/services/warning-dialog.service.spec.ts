import { TestBed, inject } from '@angular/core/testing';

import { WarningDialogService } from './warning-dialog.service';

describe('WarningDialogService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WarningDialogService]
    });
  });

  it('should be created', inject([WarningDialogService], (service: WarningDialogService) => {
    expect(service).toBeTruthy();
  }));
});
