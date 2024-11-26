import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagosRecolectoresComponent } from './pagos-recolectores.component';

describe('PagosRecolectoresComponent', () => {
  let component: PagosRecolectoresComponent;
  let fixture: ComponentFixture<PagosRecolectoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagosRecolectoresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagosRecolectoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
