import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCompositionComponent } from './editor-composition.component';

describe('EditorCompositionComponent', () => {
  let component: EditorCompositionComponent;
  let fixture: ComponentFixture<EditorCompositionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorCompositionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorCompositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
