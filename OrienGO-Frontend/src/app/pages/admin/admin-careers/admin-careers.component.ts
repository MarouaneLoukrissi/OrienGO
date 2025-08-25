import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { JobResponseDto } from '../../../model/dto/JobResponse.dto';
import { JobCategory } from '../../../model/enum/JobCategory.enum';
import { JobService } from '../../../Service/job.service';
import { JobRequestDto } from '../../../model/dto/JobRequest.dto';

@Component({
  selector: 'app-admin-careers',
  templateUrl: './admin-careers.component.html',
  styleUrls: ['./admin-careers.component.css']
})
export class AdminCareersComponent implements OnInit {
  // State management
  loading = false;
  submitting = false;
  error: string | null = null;
  success: string | null = null;

  // Modal properties
  showAddJobModal = false;
  showViewJobModal = false;
  showEditJobModal = false;
  selectedJob: JobResponseDto | null = null;
  editingJob: JobResponseDto | null = null;

  // Forms
  addJobForm!: FormGroup;
  editJobForm!: FormGroup;

  // Search and filtering properties
  searchTerm = '';
  selectedCategory = '';
  selectedJobMarket = '';
  sortField = '';
  sortDirection = 'asc';

  // Pagination properties
  currentPage = 1;
  pageSize = 5;
  totalPages = 0;
  paginatedJobs: JobResponseDto[] = [];

  // Data
  originalJobs: JobResponseDto[] = [];
  jobs: JobResponseDto[] = [];
  jobCategories = Object.values(JobCategory);

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private jobService: JobService
  ) {
    this.initializeForms();
  }

  ngOnInit(): void {
    this.loadJobs();
  }

  min(a: number, b: number): number {
    return Math.min(a, b);
  }

  private initializeForms(): void {
    this.addJobForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      category: [null, Validators.required],
      education: ['', [Validators.maxLength(100)]],
      salaryRange: ['', [Validators.maxLength(100)]],
      jobMarket: ['', [Validators.maxLength(100)]],
      tags: ['', [Validators.maxLength(500)]],
      riasecRealistic: [0, [Validators.min(0), Validators.max(100)]],
      riasecInvestigative: [0, [Validators.min(0), Validators.max(100)]],
      riasecArtistic: [0, [Validators.min(0), Validators.max(100)]],
      riasecSocial: [0, [Validators.min(0), Validators.max(100)]],
      riasecEnterprising: [0, [Validators.min(0), Validators.max(100)]],
      riasecConventional: [0, [Validators.min(0), Validators.max(100)]]
    });

    this.editJobForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      category: [null, Validators.required],
      education: ['', [Validators.maxLength(100)]],
      salaryRange: ['', [Validators.maxLength(100)]],
      jobMarket: ['', [Validators.maxLength(100)]],
      tags: ['', [Validators.maxLength(500)]],
      riasecRealistic: [0, [Validators.min(0), Validators.max(100)]],
      riasecInvestigative: [0, [Validators.min(0), Validators.max(100)]],
      riasecArtistic: [0, [Validators.min(0), Validators.max(100)]],
      riasecSocial: [0, [Validators.min(0), Validators.max(100)]],
      riasecEnterprising: [0, [Validators.min(0), Validators.max(100)]],
      riasecConventional: [0, [Validators.min(0), Validators.max(100)]]
    });
  }

  // Data loading methods
  loadJobs(): void {
    this.loading = true;
    this.error = null;
    
    this.jobService.getAllJobs().subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.originalJobs = response.data || [];
          this.applyFilters();
          this.success = 'Careers loaded successfully';
          this.clearMessages();
        } else {
          this.error = response.message || 'Failed to load careers';
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading jobs:', error);
        this.error = 'Failed to load careers. Please try again.';
        this.loading = false;
      }
    });
  }

  // Modal management methods
  openAddJobModal(): void {
    this.showAddJobModal = true;
    this.addJobForm.reset();
    this.addJobForm.patchValue({
      riasecRealistic: 0,
      riasecInvestigative: 0,
      riasecArtistic: 0,
      riasecSocial: 0,
      riasecEnterprising: 0,
      riasecConventional: 0
    });
  }

  closeAddJobModal(): void {
    this.showAddJobModal = false;
    this.addJobForm.reset();
  }

  onSubmit(): void {
    if (this.addJobForm.valid) {
      this.submitting = true;
      this.error = null;
      
      const formValue = this.addJobForm.value;
      const jobRequest: JobRequestDto = {
        title: formValue.title,
        description: formValue.description,
        category: formValue.category,
        education: formValue.education,
        salaryRange: formValue.salaryRange,
        jobMarket: formValue.jobMarket,
        tags: this.parseTags(formValue.tags),
        riasecRealistic: formValue.riasecRealistic || 0,
        riasecInvestigative: formValue.riasecInvestigative || 0,
        riasecArtistic: formValue.riasecArtistic || 0,
        riasecSocial: formValue.riasecSocial || 0,
        riasecEnterprising: formValue.riasecEnterprising || 0,
        riasecConventional: formValue.riasecConventional || 0,
        softDeleted: false
      };

      this.jobService.createJob(jobRequest).subscribe({
        next: (response) => {
          if (response.status === 200 || response.status === 201) {
            this.success = 'Career created successfully';
            this.closeAddJobModal();
            this.loadJobs(); // Reload data
            this.clearMessages();
          } else {
            this.error = response.message || 'Failed to create career';
          }
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error creating job:', error);
          this.error = 'Failed to create career. Please try again.';
          this.submitting = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.addJobForm);
    }
  }

  openViewJobModal(job: JobResponseDto): void {
    this.selectedJob = job;
    this.showViewJobModal = true;
  }

  closeViewJobModal(): void {
    this.showViewJobModal = false;
    this.selectedJob = null;
  }

  openEditJobModal(job: JobResponseDto): void {
    this.editingJob = job;
    this.editJobForm.patchValue({
      title: job.title,
      description: job.description,
      category: job.category,
      education: job.education,
      salaryRange: job.salaryRange,
      jobMarket: job.jobMarket,
      tags: job.tags ? job.tags.join(', ') : '',
      riasecRealistic: job.riasecRealistic || 0,
      riasecInvestigative: job.riasecInvestigative || 0,
      riasecArtistic: job.riasecArtistic || 0,
      riasecSocial: job.riasecSocial || 0,
      riasecEnterprising: job.riasecEnterprising || 0,
      riasecConventional: job.riasecConventional || 0
    });
    this.showEditJobModal = true;
  }

  closeEditJobModal(): void {
    this.showEditJobModal = false;
    this.editingJob = null;
    this.editJobForm.reset();
  }

  onEditSubmit(): void {
    if (this.editJobForm.valid && this.editingJob) {
      this.submitting = true;
      this.error = null;
      
      const formValue = this.editJobForm.value;
      const jobRequest: JobRequestDto = {
        title: formValue.title,
        description: formValue.description,
        category: formValue.category,
        education: formValue.education,
        salaryRange: formValue.salaryRange,
        jobMarket: formValue.jobMarket,
        tags: this.parseTags(formValue.tags),
        riasecRealistic: formValue.riasecRealistic || 0,
        riasecInvestigative: formValue.riasecInvestigative || 0,
        riasecArtistic: formValue.riasecArtistic || 0,
        riasecSocial: formValue.riasecSocial || 0,
        riasecEnterprising: formValue.riasecEnterprising || 0,
        riasecConventional: formValue.riasecConventional || 0,
        softDeleted: this.editingJob.softDeleted
      };

      this.jobService.updateJob(this.editingJob.id, jobRequest).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.success = 'Career updated successfully';
            this.closeEditJobModal();
            this.loadJobs(); // Reload data
            this.clearMessages();
          } else {
            this.error = response.message || 'Failed to update career';
          }
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error updating job:', error);
          this.error = 'Failed to update career. Please try again.';
          this.submitting = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.editJobForm);
    }
  }

  deleteJob(job: JobResponseDto): void {
    if (confirm('Are you sure you want to delete this career?')) {
      this.error = null;
      
      this.jobService.deleteJob(job.id).subscribe({
        next: (response) => {
          if (response.status === 200 || response.status === 204) {
            this.success = 'Career deleted successfully';
            this.loadJobs(); // Reload data
            this.clearMessages();
          } else {
            this.error = response.message || 'Failed to delete career';
          }
        },
        error: (error) => {
          console.error('Error deleting job:', error);
          this.error = 'Failed to delete career. Please try again.';
        }
      });
    }
  }

  // Pagination methods
  updatePagination(): void {
    this.totalPages = Math.ceil(this.jobs.length / this.pageSize);
    
    // Reset to first page if current page is out of bounds
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = 1;
    }
    
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedJobs = this.jobs.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePagination();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 3;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);
    
    // Adjust start page if we're near the end
    if (endPage - startPage < maxVisiblePages - 1) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // Utility methods
  private parseTags(tagsString: string): string[] {
    return tagsString ? tagsString.split(',').map(tag => tag.trim()).filter(tag => tag) : [];
  }

  private clearMessages(): void {
    setTimeout(() => {
      this.success = null;
      this.error = null;
    }, 3000);
  }

  markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  getErrorMessage(fieldName: string, form: FormGroup): string {
    const control = form.get(fieldName);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return 'This field is required';
      }
      if (control.errors['minlength']) {
        return `Minimum length is ${control.errors['minlength'].requiredLength} characters`;
      }
      if (control.errors['maxlength']) {
        return `Maximum length is ${control.errors['maxlength'].requiredLength} characters`;
      }
      if (control.errors['min']) {
        return `Minimum value is ${control.errors['min'].min}`;
      }
      if (control.errors['max']) {
        return `Maximum value is ${control.errors['max'].max}`;
      }
    }
    return '';
  }

  getJobCategoryLabel(category: JobCategory): string {
    const labels: { [key in JobCategory]: string } = {
      [JobCategory.HEALTH]: 'Health',
      [JobCategory.EDUCATION]: 'Education',
      [JobCategory.TECH]: 'Technology',
      [JobCategory.BUSINESS]: 'Business',
      [JobCategory.ARTS]: 'Arts'
    };
    return labels[category] || category;
  }

  // Search and filtering methods
  onSearchChange(): void {
    this.currentPage = 1; // Reset to first page when searching
    this.applyFilters();
  }

  onCategoryChange(): void {
    this.currentPage = 1; // Reset to first page when filtering
    this.applyFilters();
  }

  onJobMarketChange(): void {
    this.currentPage = 1; // Reset to first page when filtering
    this.applyFilters();
  }

  onSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.currentPage = 1; // Reset to first page when sorting
    this.applyFilters();
  }

  applyFilters(): void {
    let filteredJobs = [...this.originalJobs];

    // Search filter
    if (this.searchTerm) {
      const searchLower = this.searchTerm.toLowerCase();
      filteredJobs = filteredJobs.filter(job =>
        job.title.toLowerCase().includes(searchLower) ||
        job.description.toLowerCase().includes(searchLower) ||
        (job.education && job.education.toLowerCase().includes(searchLower))
      );
    }

    // Category filter
    if (this.selectedCategory) {
      filteredJobs = filteredJobs.filter(job => job.category === this.selectedCategory);
    }

    // Job market filter
    if (this.selectedJobMarket) {
      filteredJobs = filteredJobs.filter(job => job.jobMarket === this.selectedJobMarket);
    }

    // Sorting
    if (this.sortField) {
      filteredJobs.sort((a, b) => {
        const aValue = (a as any)[this.sortField];
        const bValue = (b as any)[this.sortField];
        
        if (aValue < bValue) return this.sortDirection === 'asc' ? -1 : 1;
        if (aValue > bValue) return this.sortDirection === 'asc' ? 1 : -1;
        return 0;
      });
    }

    this.jobs = filteredJobs;
    this.updatePagination();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.selectedJobMarket = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.currentPage = 1; // Reset to first page when resetting filters
    this.applyFilters();
  }

  getUniqueJobMarkets(): string[] {
    return [
      ...new Set(
        this.originalJobs
          .map(job => job.jobMarket)
          .filter((market): market is string => !!market) // type guard
      )
    ];
  }
}