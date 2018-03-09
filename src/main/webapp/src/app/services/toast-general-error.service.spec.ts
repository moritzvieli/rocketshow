import { TestBed, inject } from '@angular/core/testing';

import { ToastGeneralErrorService } from './toast-general-error.service';

describe('ToastGeneralErrorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ToastGeneralErrorService]
    });
  });

  it('should be created', inject([ToastGeneralErrorService], (service: ToastGeneralErrorService) => {
    expect(service).toBeTruthy();
  }));
});
