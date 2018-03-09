import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorCompositionFileComponent } from './editor-composition-file.component';

describe('EditorCompositionFileComponent', () => {
  let component: EditorCompositionFileComponent;
  let fixture: ComponentFixture<EditorCompositionFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorCompositionFileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorCompositionFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
