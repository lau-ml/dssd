import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntoCreateComponent } from './punto-create.component';

describe('PuntoCreateComponent', () => {
  let component: PuntoCreateComponent;
  let fixture: ComponentFixture<PuntoCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuntoCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PuntoCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
