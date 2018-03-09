import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorSetComponent } from './editor-set.component';

describe('EditorSetComponent', () => {
  let component: EditorSetComponent;
  let fixture: ComponentFixture<EditorSetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorSetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorSetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
