import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntosRecoleccionComponent } from './puntos-recoleccion.component';

describe('PuntosRecoleccionComponent', () => {
  let component: PuntosRecoleccionComponent;
  let fixture: ComponentFixture<PuntosRecoleccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuntosRecoleccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PuntosRecoleccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
