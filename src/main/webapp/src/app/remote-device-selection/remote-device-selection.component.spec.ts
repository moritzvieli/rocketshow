import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoteDeviceSelectionComponent } from './remote-device-selection.component';

describe('RemoteDeviceSelectionComponent', () => {
  let component: RemoteDeviceSelectionComponent;
  let fixture: ComponentFixture<RemoteDeviceSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoteDeviceSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoteDeviceSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
