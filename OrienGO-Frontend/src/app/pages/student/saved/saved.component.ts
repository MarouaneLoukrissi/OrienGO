import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { JobDTO } from '../../../model/dto/Job.dto';
import { TrainingDTO } from '../../../model/dto/Training.dto';
import { PersonalizedJobResponseDto } from '../../../model/dto/PersonalizedJobResponse.dto';
import { PersonalizedJobService } from '../../../Service/personalizedJob.service';
import { RecommendationService } from '../../../Service/recommendation.service';
import { LinkType } from '../../../model/enum/LinkType.enum';

@Component({
  selector: 'app-saved',
  templateUrl: './saved.component.html',
  styleUrl: './saved.component.css'
})
export class SavedComponent implements OnInit {
  // Three types of saved items
  savedRecommendationJobs: JobDTO[] = [];
  savedTrainings: TrainingDTO[] = [];
  savedPersonalizedJobs: PersonalizedJobResponseDto[] = [];

  // Selected items for modals
  selectedRecommendationJob: JobDTO | null = null;
  selectedTraining: TrainingDTO | null = null;
  selectedPersonalizedJob: PersonalizedJobResponseDto | null = null;

  isLoading = false;
  isRemoving = false;
  errorMessage: string | null = null;

  // This should come from your auth service or user context
  private readonly studentId = 1; // Replace with actual student ID from auth

  constructor(
    private personalizedJobService: PersonalizedJobService,
    private recommendationService: RecommendationService
  ) {}

  ngOnInit(): void {
    this.loadSavedItems();
  }

  private loadSavedItems(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Load all three types of saved items in parallel
    Promise.all([
      this.loadSavedRecommendations(),
      this.loadSavedPersonalizedJobs()
    ]).finally(() => {
      this.isLoading = false;
    });
  }

  private loadSavedRecommendations(): Promise<void> {
    return new Promise((resolve) => {
      this.recommendationService.getRecommendationsByLinkType(LinkType.SAVED, this.studentId)
        .subscribe({
          next: (response) => {
            if (response.status === 200 && response.data) {
              this.savedRecommendationJobs = response.data.jobs || [];
              this.savedTrainings = response.data.trainings || [];
            } else {
              this.savedRecommendationJobs = [];
              this.savedTrainings = [];
            }
            resolve();
          },
          error: (error) => {
            console.error('Error loading saved recommendations:', error);
            this.savedRecommendationJobs = [];
            this.savedTrainings = [];
            this.errorMessage = 'Failed to load saved recommendations. Please try again later.';
            resolve();
          }
        });
    });
  }

  private loadSavedPersonalizedJobs(): Promise<void> {
    return new Promise((resolve) => {
      this.personalizedJobService.getByLinkType(LinkType.SAVED, this.studentId)
        .subscribe({
          next: (response) => {
            if (response.status === 200 && response.data) {
              this.savedPersonalizedJobs = response.data;
            } else {
              this.savedPersonalizedJobs = [];
            }
            resolve();
          },
          error: (error) => {
            console.error('Error loading saved personalized jobs:', error);
            this.savedPersonalizedJobs = [];
            if (!this.errorMessage) {
              this.errorMessage = 'Failed to load saved personalized jobs. Please try again later.';
            }
            resolve();
          }
        });
    });
  }

  // Recommendation Job Methods
  viewRecommendationJobDetails(job: JobDTO): void {
    this.selectedRecommendationJob = job;
    document.body.style.overflow = 'hidden';
  }

  removeRecommendationJob(jobId: number): void {
    if (!jobId) return;

    this.isRemoving = true;

    const request = {
      studentId: this.studentId,
      jobId: jobId,
      type: LinkType.SAVED
    };

    this.recommendationService.unlinkJob(request)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.savedRecommendationJobs = this.savedRecommendationJobs.filter(job => job.id !== jobId);
            if (this.selectedRecommendationJob?.id === jobId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove recommendation job. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing recommendation job:', error);
          this.errorMessage = 'Failed to remove recommendation job. Please try again.';
        }
      });
  }

  // Training Methods
  viewTrainingDetails(training: TrainingDTO): void {
    this.selectedTraining = training;
    document.body.style.overflow = 'hidden';
  }

  removeTraining(trainingId: number): void {
    if (!trainingId) return;

    this.isRemoving = true;

    const request = {
      studentId: this.studentId,
      trainingId: trainingId,
      type: LinkType.SAVED
    };

    this.recommendationService.unlinkTraining(request)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.savedTrainings = this.savedTrainings.filter(training => training.id !== trainingId);
            if (this.selectedTraining?.id === trainingId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove training. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing training:', error);
          this.errorMessage = 'Failed to remove training. Please try again.';
        }
      });
  }

  // Personalized Job Methods
  viewPersonalizedJobDetails(job: PersonalizedJobResponseDto): void {
    this.selectedPersonalizedJob = job;
    document.body.style.overflow = 'hidden';
  }

  removePersonalizedJob(jobId: number): void {
    if (!jobId) return;

    this.isRemoving = true;

    this.personalizedJobService.unlinkJobFromStudent(this.studentId, jobId, LinkType.SAVED)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.savedPersonalizedJobs = this.savedPersonalizedJobs.filter(job => job.id !== jobId);
            if (this.selectedPersonalizedJob?.id === jobId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove personalized job. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing personalized job:', error);
          this.errorMessage = 'Failed to remove personalized job. Please try again.';
        }
      });
  }

  // Modal Methods
  closeModal(): void {
    this.selectedRecommendationJob = null;
    this.selectedTraining = null;
    this.selectedPersonalizedJob = null;
    document.body.style.overflow = 'auto';
  }

  // Utility Methods
  applyToJob(applyUrl: string): void {
    if (applyUrl) {
      window.open(applyUrl, '_blank');
    }
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return 'Date not available';

    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    } catch (error) {
      return 'Invalid date';
    }
  }

  // Refresh all saved items
  refreshSavedItems(): void {
    this.loadSavedItems();
  }
}
