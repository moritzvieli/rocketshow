import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MidiMappingComponent } from './midi-mapping.component';

describe('MidiMappingComponent', () => {
  let component: MidiMappingComponent;
  let fixture: ComponentFixture<MidiMappingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MidiMappingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MidiMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
