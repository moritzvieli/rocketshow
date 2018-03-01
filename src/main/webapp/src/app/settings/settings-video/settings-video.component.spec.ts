import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsVideoComponent } from './settings-video.component';

describe('SettingsVideoComponent', () => {
  let component: SettingsVideoComponent;
  let fixture: ComponentFixture<SettingsVideoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsVideoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
