import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsDmxComponent } from './settings-dmx.component';

describe('SettingsDmxComponent', () => {
  let component: SettingsDmxComponent;
  let fixture: ComponentFixture<SettingsDmxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsDmxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsDmxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
