import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HrEquipmentComponent } from './hr-equipment.component';

describe('HrEquipmentComponent', () => {
  let component: HrEquipmentComponent;
  let fixture: ComponentFixture<HrEquipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HrEquipmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HrEquipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
