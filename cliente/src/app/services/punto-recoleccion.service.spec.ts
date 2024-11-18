import { TestBed } from '@angular/core/testing';

import { PuntoDeRecoleccionService } from './punto-recoleccion.service';

describe('UbicacionesService', () => {
  let service: PuntoDeRecoleccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PuntoDeRecoleccionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
