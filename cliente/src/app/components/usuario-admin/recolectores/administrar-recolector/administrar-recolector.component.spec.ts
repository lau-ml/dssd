import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarRecolectorComponent } from './administrar-recolector.component';

describe('AdministrarRecolectorComponent', () => {
  let component: AdministrarRecolectorComponent;
  let fixture: ComponentFixture<AdministrarRecolectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrarRecolectorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarRecolectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
