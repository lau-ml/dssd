import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroRecoleccionComponent } from './registro-recoleccion.component';

describe('RegistroRecoleccionComponent', () => {
  let component: RegistroRecoleccionComponent;
  let fixture: ComponentFixture<RegistroRecoleccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroRecoleccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistroRecoleccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
