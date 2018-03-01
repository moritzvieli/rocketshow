import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsAudioComponent } from './settings-audio.component';

describe('SettingsAudioComponent', () => {
  let component: SettingsAudioComponent;
  let fixture: ComponentFixture<SettingsAudioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsAudioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsAudioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
