import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoutingDetailsComponent } from './routing-details.component';

describe('RoutingDetailsComponent', () => {
  let component: RoutingDetailsComponent;
  let fixture: ComponentFixture<RoutingDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoutingDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoutingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
