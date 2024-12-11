import { TestBed } from '@angular/core/testing';

import { HrEquipmentService } from './hr-equipment.service';

describe('HrEquipmentService', () => {
  let service: HrEquipmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HrEquipmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
