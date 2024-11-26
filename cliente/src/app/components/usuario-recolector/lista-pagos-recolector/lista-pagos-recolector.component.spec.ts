import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaPagosRecolectorComponent } from './lista-pagos-recolector.component';

describe('ListaPagosRecolectorComponent', () => {
  let component: ListaPagosRecolectorComponent;
  let fixture: ComponentFixture<ListaPagosRecolectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaPagosRecolectorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaPagosRecolectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
