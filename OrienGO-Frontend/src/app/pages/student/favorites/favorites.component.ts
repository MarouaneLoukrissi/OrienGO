import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { JobDTO } from '../../../model/dto/Job.dto';
import { TrainingDTO } from '../../../model/dto/Training.dto';
import { PersonalizedJobResponseDto } from '../../../model/dto/PersonalizedJobResponse.dto';
import { PersonalizedJobService } from '../../../Service/personalizedJob.service';
import { RecommendationService } from '../../../Service/recommendation.service';
import { LinkType } from '../../../model/enum/LinkType.enum';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.css'
})
export class FavoritesComponent implements OnInit {
  // Three types of favorite items
  favoriteRecommendationJobs: JobDTO[] = [];
  favoriteTrainings: TrainingDTO[] = [];
  favoritePersonalizedJobs: PersonalizedJobResponseDto[] = [];

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
    this.loadFavoriteItems();
  }

  private loadFavoriteItems(): void {
    this.isLoading = true;
    this.errorMessage = null;

    // Load all three types of favorite items in parallel
    Promise.all([
      this.loadFavoriteRecommendations(),
      this.loadFavoritePersonalizedJobs()
    ]).finally(() => {
      this.isLoading = false;
    });
  }

  private loadFavoriteRecommendations(): Promise<void> {
    return new Promise((resolve) => {
      this.recommendationService.getRecommendationsByLinkType(LinkType.FAVORITE, this.studentId)
        .subscribe({
          next: (response) => {
            if (response.status === 200 && response.data) {
              this.favoriteRecommendationJobs = response.data.jobs || [];
              this.favoriteTrainings = response.data.trainings || [];
            } else {
              this.favoriteRecommendationJobs = [];
              this.favoriteTrainings = [];
            }
            resolve();
          },
          error: (error) => {
            console.error('Error loading favorite recommendations:', error);
            this.favoriteRecommendationJobs = [];
            this.favoriteTrainings = [];
            this.errorMessage = 'Failed to load favorite recommendations. Please try again later.';
            resolve();
          }
        });
    });
  }

  private loadFavoritePersonalizedJobs(): Promise<void> {
    return new Promise((resolve) => {
      this.personalizedJobService.getByLinkType(LinkType.FAVORITE, this.studentId)
        .subscribe({
          next: (response) => {
            if (response.status === 200 && response.data) {
              this.favoritePersonalizedJobs = response.data;
            } else {
              this.favoritePersonalizedJobs = [];
            }
            resolve();
          },
          error: (error) => {
            console.error('Error loading favorite personalized jobs:', error);
            this.favoritePersonalizedJobs = [];
            if (!this.errorMessage) {
              this.errorMessage = 'Failed to load favorite personalized jobs. Please try again later.';
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

  removeFavoriteRecommendationJob(jobId: number): void {
    if (!jobId) return;

    this.isRemoving = true;

    const request = {
      studentId: this.studentId,
      jobId: jobId,
      type: LinkType.FAVORITE
    };

    this.recommendationService.unlinkJob(request)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.favoriteRecommendationJobs = this.favoriteRecommendationJobs.filter(job => job.id !== jobId);
            if (this.selectedRecommendationJob?.id === jobId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove favorite recommendation job. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing favorite recommendation job:', error);
          this.errorMessage = 'Failed to remove favorite recommendation job. Please try again.';
        }
      });
  }

  // Training Methods
  viewTrainingDetails(training: TrainingDTO): void {
    this.selectedTraining = training;
    document.body.style.overflow = 'hidden';
  }

  removeFavoriteTraining(trainingId: number): void {
    if (!trainingId) return;

    this.isRemoving = true;

    const request = {
      studentId: this.studentId,
      trainingId: trainingId,
      type: LinkType.FAVORITE
    };

    this.recommendationService.unlinkTraining(request)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.favoriteTrainings = this.favoriteTrainings.filter(training => training.id !== trainingId);
            if (this.selectedTraining?.id === trainingId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove favorite training. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing favorite training:', error);
          this.errorMessage = 'Failed to remove favorite training. Please try again.';
        }
      });
  }

  // Personalized Job Methods
  viewPersonalizedJobDetails(job: PersonalizedJobResponseDto): void {
    this.selectedPersonalizedJob = job;
    document.body.style.overflow = 'hidden';
  }

  removeFavoritePersonalizedJob(jobId: number): void {
    if (!jobId) return;

    this.isRemoving = true;

    this.personalizedJobService.unlinkJobFromStudent(this.studentId, jobId, LinkType.FAVORITE)
      .pipe(finalize(() => this.isRemoving = false))
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.favoritePersonalizedJobs = this.favoritePersonalizedJobs.filter(job => job.id !== jobId);
            if (this.selectedPersonalizedJob?.id === jobId) {
              this.closeModal();
            }
          } else {
            this.errorMessage = 'Failed to remove favorite personalized job. Please try again.';
          }
        },
        error: (error) => {
          console.error('Error removing favorite personalized job:', error);
          this.errorMessage = 'Failed to remove favorite personalized job. Please try again.';
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

  // Refresh all favorite items
  refreshFavoriteItems(): void {
    this.loadFavoriteItems();
  }

  // Legacy methods for backward compatibility (now call the new methods)
  viewJobDetails(jobId: string): void {
    const job = this.favoriteRecommendationJobs.find(j => j.id?.toString() === jobId);
    if (job) {
      this.viewRecommendationJobDetails(job);
    }
  }

  toggleJobVisibility(jobId: string): void {
    const numericId = parseInt(jobId);
    if (!isNaN(numericId)) {
      this.removeFavoriteRecommendationJob(numericId);
    }
  }

  toggleTrainingVisibility(trainingId: string): void {
    const numericId = parseInt(trainingId);
    if (!isNaN(numericId)) {
      this.removeFavoriteTraining(numericId);
    }
  }
}
