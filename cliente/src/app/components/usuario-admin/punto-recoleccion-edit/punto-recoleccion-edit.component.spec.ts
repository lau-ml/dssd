import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntoRecoleccionEditComponent } from './punto-recoleccion-edit.component';

describe('PuntoRecoleccionEditComponent', () => {
  let component: PuntoRecoleccionEditComponent;
  let fixture: ComponentFixture<PuntoRecoleccionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuntoRecoleccionEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PuntoRecoleccionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
