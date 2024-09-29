import { TestBed } from '@angular/core/testing';

import { RegistroRecoleccionService } from './registro-recoleccion.service';

describe('RegistroRecoleccionService', () => {
  let service: RegistroRecoleccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistroRecoleccionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
