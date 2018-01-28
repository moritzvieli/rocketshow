import { TestBed, inject } from '@angular/core/testing';

import { InfoDialogService } from './info-dialog.service';

describe('InfoDialogService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InfoDialogService]
    });
  });

  it('should be created', inject([InfoDialogService], (service: InfoDialogService) => {
    expect(service).toBeTruthy();
  }));
});
