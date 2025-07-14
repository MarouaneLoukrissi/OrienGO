import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RiasecTestPageComponent } from './riasec-test-page.component';

describe('RiasecTestPageComponent', () => {
  let component: RiasecTestPageComponent;
  let fixture: ComponentFixture<RiasecTestPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RiasecTestPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RiasecTestPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
