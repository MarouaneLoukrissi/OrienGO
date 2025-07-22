import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FullTestComponent } from './full-test.component';

describe('FullTestComponent', () => {
  let component: FullTestComponent;
  let fixture: ComponentFixture<FullTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FullTestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FullTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
