import { TestBed, inject } from '@angular/core/testing';

import { CompositionService } from './composition.service';

describe('CompositionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CompositionService]
    });
  });

  it('should be created', inject([CompositionService], (service: CompositionService) => {
    expect(service).toBeTruthy();
  }));
});
