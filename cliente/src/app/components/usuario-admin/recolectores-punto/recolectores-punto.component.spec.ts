import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecolectoresPuntoComponent } from './recolectores-punto.component';

describe('RecolectoresPuntoComponent', () => {
  let component: RecolectoresPuntoComponent;
  let fixture: ComponentFixture<RecolectoresPuntoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecolectoresPuntoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecolectoresPuntoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
