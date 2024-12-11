import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmComponent } from './em.component';

describe('EmComponent', () => {
  let component: EmComponent;
  let fixture: ComponentFixture<EmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
