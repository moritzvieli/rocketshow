import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorSetlistComponent } from './editor-setlist.component';

describe('EditorSetlistComponent', () => {
  let component: EditorSetlistComponent;
  let fixture: ComponentFixture<EditorSetlistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorSetlistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorSetlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
