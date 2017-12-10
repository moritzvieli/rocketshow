import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorSongFileComponent } from './editor-song-file.component';

describe('EditorSongFileComponent', () => {
  let component: EditorSongFileComponent;
  let fixture: ComponentFixture<EditorSongFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorSongFileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorSongFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
