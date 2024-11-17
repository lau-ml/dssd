import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecolectoresListComponent } from './recolectores-list.component';

describe('RecolectoresListComponent', () => {
  let component: RecolectoresListComponent;
  let fixture: ComponentFixture<RecolectoresListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecolectoresListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecolectoresListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
