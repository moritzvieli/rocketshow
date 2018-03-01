import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsMidiComponent } from './settings-midi.component';

describe('SettingsMidiComponent', () => {
  let component: SettingsMidiComponent;
  let fixture: ComponentFixture<SettingsMidiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsMidiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsMidiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
