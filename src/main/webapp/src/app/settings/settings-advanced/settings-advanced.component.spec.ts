import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsAdvancedComponent } from './settings-advanced.component';

describe('SettingsAdvancedComponent', () => {
  let component: SettingsAdvancedComponent;
  let fixture: ComponentFixture<SettingsAdvancedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsAdvancedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsAdvancedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
