import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarMaterialComponent } from './cargar-material.component';

describe('CargarMaterialComponent', () => {
  let component: CargarMaterialComponent;
  let fixture: ComponentFixture<CargarMaterialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CargarMaterialComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CargarMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
