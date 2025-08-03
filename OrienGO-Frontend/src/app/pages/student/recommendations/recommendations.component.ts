import { Component } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { Career, EducationPath, RecommendationsResponse } from '../../../interfaces/career.interface';
import { RecommendationsService } from '../../../Service/recommendations.service';

@Component({
  selector: 'app-recommendations',
  templateUrl: './recommendations.component.html',
  styleUrl: './recommendations.component.css'
})
export class RecommendationsComponent {
  careers: Career[] = [];
  educationPaths: EducationPath[] = [];
  userProfile: any = null;
  loading = true;
  error: string | null = null;

  private destroy$ = new Subject<void>();
  private currentUserId = 'user123'; // This would come from authentication service

  constructor(private recommendationsService: RecommendationsService) {}

  ngOnInit() {
    this.loadRecommendations();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadRecommendations() {
    this.loading = true;
    this.error = null;

    this.recommendationsService.getRecommendations(this.currentUserId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: RecommendationsResponse) => {
          this.careers = response.careers;
          this.educationPaths = response.educationPaths;
          this.userProfile = response.userProfile;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading recommendations:', error);
          this.error = 'Failed to load recommendations. Please try again.';
          this.loading = false;
        }
      });
  }

  // Event handlers for backend integration
  onCareerClick(career: Career) {
    console.log('Career clicked:', career);
    // Navigate to career details page or open modal
    // this.router.navigate(['/careers', career.id]);
  }

  onEducationClick(education: EducationPath) {
    console.log('Education path clicked:', education);
    // Navigate to education details page or open modal
    // this.router.navigate(['/education', education.id]);
  }

  onLearnMoreClick(career: Career, event: Event) {
    event.stopPropagation();
    console.log('Learn more clicked for:', career);
    // Track user interaction for analytics
    // this.analyticsService.track('career_learn_more_clicked', { careerId: career.id });
  }

  onExploreJobProfiles() {
    console.log('Explore job profiles clicked');
    // Navigate to job profiles page
    // this.router.navigate(['/job-profiles']);
  }

  // TrackBy functions for performance optimization
  trackByCareer(index: number, career: Career): string {
    return career.id;
  }

  trackByEducation(index: number, education: EducationPath): string {
    return education.id;
  }
}
