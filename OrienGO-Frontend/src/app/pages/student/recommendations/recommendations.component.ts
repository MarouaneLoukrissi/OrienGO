import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Career, EducationPath, RecommendationsResponse } from '../../../interfaces/career.interface';
import { JobDTO } from '../../../model/dto/Job.dto';
import { TrainingDTO } from '../../../model/dto/Training.dto';
import { JobCategory } from '../../../model/enum/JobCategory.enum';
import { TrainingType } from '../../../model/enum/TrainingType.enum';
import { LinkType } from '../../../model/enum/LinkType.enum';
import { RecommendationService } from '../../../Service/recommendation.service';
import { StudentJobLinkRequestDto } from '../../../model/dto/StudentJobLinkRequest.dto';
import { StudentTrainingLinkRequestDto } from '../../../model/dto/StudentTrainingLinkRequest.dto';

@Component({
  selector: 'app-recommendations',
  templateUrl: './recommendations.component.html',
  styleUrl: './recommendations.component.css'
})
export class RecommendationsComponent implements OnInit, OnDestroy {
  careers: Career[] = [];
  educationPaths: EducationPath[] = [];
  userProfile: any = null;
  loading = true;
  error: string | null = null;
  testResultId: number | null = null;
  studentId: number = 1; // This would come from authentication service

  // Ownership check - determines if user can perform save/favorite actions
  isOwner = true; // This should be determined from authentication service or route params

  // Modal state
  showModal = false;
  selectedCareer: Career | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private recommendationService: RecommendationService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.determineOwnership();
    // Extract testResultId from URL parameters
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      const testResultId = params['testResultId'];
      if (testResultId) {
        this.testResultId = parseInt(testResultId, 10);
        if (!isNaN(this.testResultId)) {
          this.loadRecommendations();
        } else {
          this.error = 'Invalid test result ID format in URL.';
          this.loading = false;
        }
      } else {
        this.error = 'No test result ID found in URL. Please ensure you have a valid test result.';
        this.loading = false;
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private determineOwnership() {
    // Check route parameters or authentication context to determine if this is a coach view
    this.route.queryParams.subscribe(params => {
      // If there's a 'coach' parameter, this is a coach viewing student's recommendations
      this.isOwner = !params['coach'] || params['coach'] !== 'true';
      console.log('User is owner:', this.isOwner);
    });
  }

  loadRecommendations() {
    if (!this.testResultId) {
      this.error = 'Invalid test result ID';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.error = null;

    // First, try to fetch existing recommendations
    this.recommendationService.fetchExistingRecommendations(this.testResultId, this.studentId,)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.code === 'SUCCESS' && response.data) {
            // Recommendations exist, map and display them
            this.mapAndDisplayRecommendations(response.data);
            this.loading = false;
          } else {
            // No existing recommendations, generate new ones
            this.generateRecommendations();
          }
        },
        error: (error) => {
          console.warn('No existing recommendations found, generating new ones:', error);
          // If fetching fails, try to generate new recommendations
          this.generateRecommendations();
        }
      });
  }

  private generateRecommendations() {
    if (!this.testResultId) {
      this.error = 'Invalid test result ID';
      this.loading = false;
      return;
    }

    this.recommendationService.processRecommendations(this.studentId, this.testResultId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.code === 'SUCCESS' && response.data) {
            this.mapAndDisplayRecommendations(response.data);
          } else {
            this.error = response.message || 'Failed to generate recommendations';
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Error generating recommendations:', error);
          this.error = 'Failed to generate recommendations. Please try again.';
          this.loading = false;
        }
      });
  }

  private mapAndDisplayRecommendations(data: any) {
    // Map JobDTO to Career interface
    this.careers = (data.jobs || []).map((job: JobDTO) => this.mapJobToCareer(job));

    // Map TrainingDTO to EducationPath interface
    this.educationPaths = (data.trainings || []).map((training: TrainingDTO) => this.mapTrainingToEducationPath(training));

    // Set user profile info
    this.userProfile = {
      riasecProfile: 'Based on your test results'
    };
  }

  get jobRecommendationQueryParams() {
    return {
      ids: this.careers
        .map(c => c.jobRecommendationId)
        .filter(id => id != null) // avoid nulls
        .join(',')
    };
  }

  private mapJobToCareer(job: JobDTO): Career {
    // Extract save/favorite status from linkTypes
    const linkTypes = job.linkType || [];
    const isSaved = linkTypes.includes(LinkType.SAVED);
    const isFavorite = linkTypes.includes(LinkType.FAVORITE);

    return {
      id: job.id?.toString() || Math.random().toString(),
      title: job.title,
      description: job.description,
      category: this.getCategoryDisplay(job.category),
      categoryColor: this.getCategoryColor(job.category),
      education: job.education || 'Various levels',
      salary: job.salaryRange || 'Competitive',
      jobMarket: job.jobMarket || 'Growing',
      skills: job.tags || [],
      matchPercentage: job.matchPercentage || 85,
      jobRecommendationId: job.jobRecommendationId,
      isSaved: isSaved,
      isFavorite: isFavorite,
      backendJobId: job.id // Store backend ID for API calls
    };
  }

  private mapTrainingToEducationPath(training: TrainingDTO): EducationPath {
    // Extract save/favorite status from linkTypes
    const linkTypes = training.linkType || [];
    const isSaved = linkTypes.includes(LinkType.SAVED);
    const isFavorite = linkTypes.includes(LinkType.FAVORITE);

    return {
      id: training.id?.toString() || Math.random().toString(),
      title: training.name,
      description: training.description || 'Professional training program',
      duration: training.duration || '2-4 years',
      specializations: training.specializations || [],
      matchPercentage: training.matchPercentage || 80,
      isSaved: isSaved,
      isFavorite: isFavorite,
      backendTrainingId: training.id // Store backend ID for API calls
    };
  }

  private getCategoryDisplay(category: JobCategory): string {
    const categoryMap = {
      [JobCategory.HEALTH]: 'Healthcare',
      [JobCategory.EDUCATION]: 'Education',
      [JobCategory.TECH]: 'Technology',
      [JobCategory.BUSINESS]: 'Business',
      [JobCategory.ARTS]: 'Arts & Design',
      [JobCategory.SOCIAL]: 'Social Services'
    };
    return categoryMap[category] || 'General';
  }

  private getCategoryColor(category: JobCategory): string {
    const colorMap = {
      [JobCategory.HEALTH]: 'bg-green-100 text-green-800',
      [JobCategory.EDUCATION]: 'bg-blue-100 text-blue-800',
      [JobCategory.TECH]: 'bg-purple-100 text-purple-800',
      [JobCategory.BUSINESS]: 'bg-gray-100 text-gray-800',
      [JobCategory.ARTS]: 'bg-pink-100 text-pink-800',
      [JobCategory.SOCIAL]: 'bg-yellow-100 text-yellow-800'
    };
    return colorMap[category] || 'bg-gray-100 text-gray-800';
  }

  private getTrainingTypeDisplay(type: TrainingType): string {
    const typeMap = {
      [TrainingType.UNIVERSITY]: 'University Degree',
      [TrainingType.VOCATIONAL]: 'Vocational Training',
      [TrainingType.BOOTCAMP]: 'Intensive Bootcamp',
      [TrainingType.CERTIFICATION]: 'Professional Certification',
      [TrainingType.ONLINE_COURSE]: 'Online Course',
      [TrainingType.INTERNSHIP]: 'Internship Program',
      [TrainingType.APPRENTICESHIP]: 'Apprenticeship',
      [TrainingType.WORKSHOP]: 'Workshop',
      [TrainingType.SEMINAR]: 'Seminar',
      [TrainingType.SELF_TAUGHT]: 'Self-Directed Learning'
    };
    return typeMap[type] || 'Educational Program';
  }

  // Check if current user can perform save/favorite actions
  canPerformActions(): boolean {
    return this.isOwner;
  }

  // Career/Job Actions
  toggleJobFavorite(career: Career) {
    if (!this.canPerformActions() || !career.backendJobId) return;

    const currentlyFavorite = career.isFavorite;
    const linkType = LinkType.FAVORITE;

    // Optimistically update UI
    career.isFavorite = !currentlyFavorite;

    if (currentlyFavorite) {
      // Remove favorite
      const request: StudentJobLinkRequestDto = {
        studentId: this.studentId,
        jobId: career.backendJobId,
        type: linkType
      };

      this.recommendationService.unlinkJob(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully removed job favorite:', response);
          },
          error: (error) => {
            console.error('Error removing job favorite:', error);
            // Revert on error
            career.isFavorite = currentlyFavorite;
          }
        });
    } else {
      // Add favorite
      const request: StudentJobLinkRequestDto = {
        studentId: this.studentId,
        jobId: career.backendJobId,
        type: linkType
      };

      this.recommendationService.linkJob(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully added job favorite:', response);
          },
          error: (error) => {
            console.error('Error adding job favorite:', error);
            // Revert on error
            career.isFavorite = currentlyFavorite;
          }
        });
    }
  }

  toggleJobSave(career: Career) {
    if (!this.canPerformActions() || !career.backendJobId) return;

    const currentlySaved = career.isSaved;
    const linkType = LinkType.SAVED;

    // Optimistically update UI
    career.isSaved = !currentlySaved;

    if (currentlySaved) {
      // Remove save
      const request: StudentJobLinkRequestDto = {
        studentId: this.studentId,
        jobId: career.backendJobId,
        type: linkType
      };

      this.recommendationService.unlinkJob(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully removed job save:', response);
          },
          error: (error) => {
            console.error('Error removing job save:', error);
            // Revert on error
            career.isSaved = currentlySaved;
          }
        });
    } else {
      // Add save
      const request: StudentJobLinkRequestDto = {
        studentId: this.studentId,
        jobId: career.backendJobId,
        type: linkType
      };

      this.recommendationService.linkJob(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully added job save:', response);
          },
          error: (error) => {
            console.error('Error adding job save:', error);
            // Revert on error
            career.isSaved = currentlySaved;
          }
        });
    }
  }

  // Training Actions
  toggleTrainingFavorite(education: EducationPath) {
    if (!this.canPerformActions() || !education.backendTrainingId) return;

    const currentlyFavorite = education.isFavorite;
    const linkType = LinkType.FAVORITE;

    // Optimistically update UI
    education.isFavorite = !currentlyFavorite;

    if (currentlyFavorite) {
      // Remove favorite
      const request: StudentTrainingLinkRequestDto = {
        studentId: this.studentId,
        trainingId: education.backendTrainingId,
        type: linkType
      };

      this.recommendationService.unlinkTraining(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully removed training favorite:', response);
          },
          error: (error) => {
            console.error('Error removing training favorite:', error);
            // Revert on error
            education.isFavorite = currentlyFavorite;
          }
        });
    } else {
      // Add favorite
      const request: StudentTrainingLinkRequestDto = {
        studentId: this.studentId,
        trainingId: education.backendTrainingId,
        type: linkType
      };

      this.recommendationService.linkTraining(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully added training favorite:', response);
          },
          error: (error) => {
            console.error('Error adding training favorite:', error);
            // Revert on error
            education.isFavorite = currentlyFavorite;
          }
        });
    }
  }

  toggleTrainingSave(education: EducationPath) {
    if (!this.canPerformActions() || !education.backendTrainingId) return;

    const currentlySaved = education.isSaved;
    const linkType = LinkType.SAVED;

    // Optimistically update UI
    education.isSaved = !currentlySaved;

    if (currentlySaved) {
      // Remove save
      const request: StudentTrainingLinkRequestDto = {
        studentId: this.studentId,
        trainingId: education.backendTrainingId,
        type: linkType
      };

      this.recommendationService.unlinkTraining(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully removed training save:', response);
          },
          error: (error) => {
            console.error('Error removing training save:', error);
            // Revert on error
            education.isSaved = currentlySaved;
          }
        });
    } else {
      // Add save
      const request: StudentTrainingLinkRequestDto = {
        studentId: this.studentId,
        trainingId: education.backendTrainingId,
        type: linkType
      };

      this.recommendationService.linkTraining(request)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Successfully added training save:', response);
          },
          error: (error) => {
            console.error('Error adding training save:', error);
            // Revert on error
            education.isSaved = currentlySaved;
          }
        });
    }
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

  onSeeMoreClick(career: Career, event: Event) {
    event.stopPropagation();
    this.selectedCareer = career;
    this.showModal = true;
    console.log('See more clicked for:', career);
    // Track user interaction for analytics
    // this.analyticsService.track('career_see_more_clicked', { careerId: career.id });
  }

  closeModal() {
    this.showModal = false;
    this.selectedCareer = null;
  }

  onModalBackdropClick(event: Event) {
    if (event.target === event.currentTarget) {
      this.closeModal();
    }
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
