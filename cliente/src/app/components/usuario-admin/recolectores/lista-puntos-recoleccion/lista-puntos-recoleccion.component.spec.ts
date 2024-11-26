import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaPuntosRecoleccionComponent } from './lista-puntos-recoleccion.component';

describe('ListaPuntosRecoleccionComponent', () => {
  let component: ListaPuntosRecoleccionComponent;
  let fixture: ComponentFixture<ListaPuntosRecoleccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaPuntosRecoleccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaPuntosRecoleccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
