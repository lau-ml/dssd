import { TestBed } from '@angular/core/testing';
import { RecolectorServiceService } from './recolector-service.service';


describe('RecolectorServiceService', () => {
  let service: RecolectorServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecolectorServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
