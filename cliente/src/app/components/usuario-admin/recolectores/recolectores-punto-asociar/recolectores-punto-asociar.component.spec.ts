import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecolectoresPuntoAsociarComponent } from './recolectores-punto-asociar.component';

describe('RecolectoresPuntoAsociarComponent', () => {
  let component: RecolectoresPuntoAsociarComponent;
  let fixture: ComponentFixture<RecolectoresPuntoAsociarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecolectoresPuntoAsociarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecolectoresPuntoAsociarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
