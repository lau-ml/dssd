import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecolectoresNoAsociadosPuntoComponent } from './recolectores-no-asociados-punto.component';

describe('RecolectoresNoAsociadosPuntoComponent', () => {
  let component: RecolectoresNoAsociadosPuntoComponent;
  let fixture: ComponentFixture<RecolectoresNoAsociadosPuntoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecolectoresNoAsociadosPuntoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecolectoresNoAsociadosPuntoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
