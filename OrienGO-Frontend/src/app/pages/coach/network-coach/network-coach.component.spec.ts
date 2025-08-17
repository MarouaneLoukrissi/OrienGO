import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkCoachComponent } from './network-coach.component';

describe('NetworkCoachComponent', () => {
  let component: NetworkCoachComponent;
  let fixture: ComponentFixture<NetworkCoachComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NetworkCoachComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NetworkCoachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
