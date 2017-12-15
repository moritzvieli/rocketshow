import { TestBed, inject } from '@angular/core/testing';

import { RemoteDeviceService } from './remote-device.service';

describe('RemoteDeviceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RemoteDeviceService]
    });
  });

  it('should be created', inject([RemoteDeviceService], (service: RemoteDeviceService) => {
    expect(service).toBeTruthy();
  }));
});
