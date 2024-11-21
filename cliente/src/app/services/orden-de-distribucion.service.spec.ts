import { TestBed } from '@angular/core/testing';

import { OrdenDeDistribucionService } from './orden-de-distribucion.service';

describe('OrdenDeDistribucionService', () => {
  let service: OrdenDeDistribucionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrdenDeDistribucionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
