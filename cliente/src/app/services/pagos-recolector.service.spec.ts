import { TestBed } from '@angular/core/testing';

import { PagosRecolectorService } from './pagos-recolector.service';

describe('PagosRecolectorService', () => {
  let service: PagosRecolectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PagosRecolectorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
