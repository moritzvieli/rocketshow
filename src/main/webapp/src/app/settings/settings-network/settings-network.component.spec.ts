import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsNetworkComponent } from './settings-network.component';

describe('SettingsNetworkComponent', () => {
  let component: SettingsNetworkComponent;
  let fixture: ComponentFixture<SettingsNetworkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsNetworkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsNetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
