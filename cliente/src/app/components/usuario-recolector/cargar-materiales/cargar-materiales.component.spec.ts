import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarMaterialesComponent } from './cargar-materiales.component';

describe('CargarMaterialComponent', () => {
  let component: CargarMaterialesComponent;
  let fixture: ComponentFixture<CargarMaterialesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CargarMaterialesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CargarMaterialesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
