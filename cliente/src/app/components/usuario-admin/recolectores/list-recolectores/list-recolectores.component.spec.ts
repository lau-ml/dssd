import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRecolectoresComponent } from './list-recolectores.component';

describe('ListRecolectoresComponent', () => {
  let component: ListRecolectoresComponent;
  let fixture: ComponentFixture<ListRecolectoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListRecolectoresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListRecolectoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
