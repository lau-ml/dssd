import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitudPuntoRecoleccionComponent } from './solicitud-punto-recoleccion.component';

describe('SolicitudPuntoRecoleccionComponent', () => {
  let component: SolicitudPuntoRecoleccionComponent;
  let fixture: ComponentFixture<SolicitudPuntoRecoleccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitudPuntoRecoleccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SolicitudPuntoRecoleccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
