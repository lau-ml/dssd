import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Recoleccion_cargarComponent } from './recoleccion_cargar.component';

describe('RecoleccionCargarComponent', () => {
  let component: Recoleccion_cargarComponent;
  let fixture: ComponentFixture<Recoleccion_cargarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Recoleccion_cargarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Recoleccion_cargarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
