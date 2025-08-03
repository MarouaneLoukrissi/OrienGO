import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobsRecommendationComponent } from './jobs-recommendation.component';

describe('JobsComponent', () => {
  let component: JobsRecommendationComponent;
  let fixture: ComponentFixture<JobsRecommendationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobsRecommendationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JobsRecommendationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
