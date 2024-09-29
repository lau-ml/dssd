import { TestBed } from '@angular/core/testing';

import { DetalleRegistroRecoleccionService } from './detalle-registro-recoleccion.service';

describe('DetalleRegistroRecoleccionService', () => {
  let service: DetalleRegistroRecoleccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleRegistroRecoleccionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
