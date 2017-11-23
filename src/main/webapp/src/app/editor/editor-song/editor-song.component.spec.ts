import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorSongComponent } from './editor-song.component';

describe('EdtorSongComponent', () => {
  let component: EditorSongComponent;
  let fixture: ComponentFixture<EditorSongComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorSongComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorSongComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
