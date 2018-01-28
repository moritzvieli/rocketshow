import { TestBed, inject } from '@angular/core/testing';

import { WaitDialogService } from './wait-dialog.service';

describe('WaitDialogService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WaitDialogService]
    });
  });

  it('should be created', inject([WaitDialogService], (service: WaitDialogService) => {
    expect(service).toBeTruthy();
  }));
});
