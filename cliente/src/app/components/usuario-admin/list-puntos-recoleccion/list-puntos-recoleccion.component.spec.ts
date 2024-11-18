import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListPuntosRecoleccionComponent } from './list-puntos-recoleccion.component';

describe('ListPuntosRecoleccionComponent', () => {
  let component: ListPuntosRecoleccionComponent;
  let fixture: ComponentFixture<ListPuntosRecoleccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListPuntosRecoleccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListPuntosRecoleccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
