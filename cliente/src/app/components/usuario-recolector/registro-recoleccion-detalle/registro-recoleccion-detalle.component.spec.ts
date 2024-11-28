import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroRecoleccionDetalleComponent } from './registro-recoleccion-detalle.component';

describe('RegistroRecoleccionDetalleComponent', () => {
  let component: RegistroRecoleccionDetalleComponent;
  let fixture: ComponentFixture<RegistroRecoleccionDetalleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroRecoleccionDetalleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistroRecoleccionDetalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
