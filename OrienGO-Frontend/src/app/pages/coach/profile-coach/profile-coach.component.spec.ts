import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileCoachComponent } from './profile-coach.component';

describe('PublicProfileComponent', () => {
  let component: ProfileCoachComponent;
  let fixture: ComponentFixture<ProfileCoachComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileCoachComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileCoachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
