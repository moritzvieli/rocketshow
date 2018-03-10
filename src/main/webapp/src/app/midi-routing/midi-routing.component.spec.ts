import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MidiRoutingComponent } from './midi-routing.component';

describe('MidiRoutingComponent', () => {
  let component: MidiRoutingComponent;
  let fixture: ComponentFixture<MidiRoutingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MidiRoutingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MidiRoutingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
