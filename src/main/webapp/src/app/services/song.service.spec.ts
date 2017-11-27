import { TestBed, inject } from '@angular/core/testing';

import { SongService } from './song.service';

describe('SongService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SongService]
    });
  });

  it('should be created', inject([SongService], (service: SongService) => {
    expect(service).toBeTruthy();
  }));
});
