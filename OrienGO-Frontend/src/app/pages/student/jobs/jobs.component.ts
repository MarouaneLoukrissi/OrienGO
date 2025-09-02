import { Component, OnInit } from '@angular/core';
import { catchError, finalize, of } from 'rxjs';
import { PersonalizedJobService } from '../../../Service/personalizedJob.service';
import { PersonalizedJobResponseDto } from '../../../model/dto/PersonalizedJobResponse.dto';

export interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  description: string;
  salaryRange: string;
  experience: string;
  employmentType: string;
  demandLevel: 'stable' | 'high' | 'very-high';
  matchPercentage: number;
  requiredSkills: string[];
  advantages: string[];
  isSaved: boolean;
  isFavorite: boolean;
  applyUrl?: string;
  source?: string;
  postedDate?: string;
  // Additional fields from backend
  companyUrl?: string;
  companyUrlDirect?: string;
  companyAddresses?: string;
  companyNumEmployees?: number;
  companyRevenue?: string;
  companyDescription?: string;
  emails?: string;
  companyIndustry?: string;
  jobUrlDirect?: string;
  isRemote?: boolean;
  createdAt?: string;
  expirationDate?: string;
  duration?: string;
  highlighted?: boolean;
  jobRecommendationId?: number;
  softDeleted?: boolean;
  category?: string;
}

export interface SearchFilters {
  searchTerm: string;
  sector: string;
  level: string;
}

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrl: './jobs.component.css'
})
export class JobsComponent implements OnInit {
  jobs: Job[] = [];
  filteredJobs: Job[] = [];
  paginatedJobs: Job[] = [];
  filters: SearchFilters = {
    searchTerm: '',
    sector: '',
    level: ''
  };

  // Pagination properties
  currentPage = 1;
  itemsPerPage = 8;
  totalPages = 0;

  // Modal properties
  showJobModal = false;
  selectedJob: Job | null = null;

  // Loading and error states
  isLoading = false;
  error: string | null = null;
  studentId = 1; // This should be dynamic based on logged-in user

  constructor(private personalizedJobService: PersonalizedJobService) {}

  ngOnInit() {
    console.log('JobsComponent initialized');
    this.loadJobs();
  }

  private loadJobs() {
    console.log('Loading jobs for student ID:', this.studentId);
    this.isLoading = true;
    this.error = null;
    this.jobs = [];
    this.filteredJobs = [];
    this.paginatedJobs = [];

    this.personalizedJobService.getByStudentId(this.studentId)
      .pipe(
        catchError(error => {
          console.error('Error fetching jobs:', error);
          this.error = 'Failed to fetch your personalized jobs. Please try again.';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe({
        next: (response) => {
          console.log('Jobs response received:', response);

          if (response?.data) {
            this.processJobsData(response.data);
          } else {
            this.jobs = [];
            this.filteredJobs = [];
            this.updatePagination();
          }
        },
        error: (err) => {
          console.error('Subscription error:', err);
          this.error = 'An unexpected error occurred while loading jobs.';
        }
      });
  }

  private processJobsData(jobsData: PersonalizedJobResponseDto[]) {
    console.log('Processing jobs data:', jobsData?.length, 'jobs');

    if (!jobsData || !Array.isArray(jobsData)) {
      console.error('Invalid jobs data received');
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
      return;
    }

    try {
      // Filter out fallback jobs and map DTOs to Job interface
      const realJobs = jobsData.filter(dto => !this.isFallbackJob(dto));
      console.log(`Filtered out ${jobsData.length - realJobs.length} fallback jobs`);

      this.jobs = realJobs.map(dto => this.mapDtoToJob(dto));
      console.log('Successfully mapped', this.jobs.length, 'jobs');

      // Set filtered jobs to all jobs initially
      this.filteredJobs = [...this.jobs];

      // Update pagination
      this.updatePagination();

    } catch (error) {
      console.error('Error during job mapping:', error);
      this.error = 'Error processing job data';
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
    }
  }

  private isFallbackJob(dto: PersonalizedJobResponseDto): boolean {
    return !!(
      dto.source === 'FALLBACK' ||
      dto.companyName === 'Fallback Company' ||
      dto.location === 'Fallback Location' ||
      (dto.title && dto.title.toLowerCase().includes('fallback job')) ||
      (dto.description && dto.description.toLowerCase().includes('fallback job created due to scraping failure'))
    );
  }

  private mapDtoToJob(dto: PersonalizedJobResponseDto): Job {
    // Safely parse required skills
    let requiredSkills: string[] = [];
    try {
      if (dto.requiredSkills) {
        requiredSkills = dto.requiredSkills
          .split(',')
          .map((skill: string) => skill.trim())
          .filter((skill: string) => skill.length > 0);
      }
    } catch (error) {
      console.warn('Error parsing required skills:', error);
      requiredSkills = [];
    }

    // Safely parse advantages
    let advantages: string[] = [];
    try {
      if (dto.advantages) {
        advantages = dto.advantages.filter((adv: string) => adv && adv.length > 0);
      }
    } catch (error) {
      console.warn('Error parsing advantages:', error);
      advantages = [];
    }

    return {
      id: dto.id?.toString() || Math.random().toString(),
      title: dto.title || 'Job Title Not Available',
      company: dto.companyName || 'Company Not Specified',
      location: dto.location || 'Location Not Specified',
      description: dto.description || 'No description available',
      salaryRange: this.formatSalaryRange(dto.salaryRange),
      experience: dto.experienceRange || 'Not specified',
      employmentType: this.formatJobType(dto.jobType),
      demandLevel: this.getDemandLevelFromCategory(dto.category),
      matchPercentage: dto.matchPercentage || 0,
      requiredSkills: requiredSkills,
      advantages: advantages,
      isSaved: false,
      isFavorite: false,
      applyUrl: dto.applyUrl,
      source: dto.source,
      postedDate: dto.postedDate,
      // Additional fields
      companyUrl: dto.companyUrl,
      companyUrlDirect: dto.companyUrlDirect,
      companyAddresses: dto.companyAddresses,
      companyNumEmployees: dto.companyNumEmployees,
      companyRevenue: dto.companyRevenue,
      companyDescription: dto.companyDescription,
      emails: dto.emails,
      companyIndustry: dto.companyIndustry,
      jobUrlDirect: dto.jobUrlDirect,
      isRemote: dto.isRemote,
      createdAt: dto.createdAt,
      expirationDate: dto.expirationDate,
      duration: dto.duration,
      highlighted: dto.highlighted,
      jobRecommendationId: dto.jobRecommendationId,
      softDeleted: dto.softDeleted,
      category: dto.category
    };
  }

  private formatSalaryRange(salaryRange?: string): string {
    if (!salaryRange || salaryRange === '0 - 0 /' || salaryRange === '0 - 0') {
      return 'Salary not disclosed';
    }
    return salaryRange;
  }

  private formatJobType(jobType?: string): string {
    if (!jobType) {
      return 'Not specified';
    }

    const jobTypeMap: { [key: string]: string } = {
      'FULL_TIME': 'Full Time',
      'PART_TIME': 'Part Time',
      'CONTRACT': 'Contract',
      'INTERNSHIP': 'Internship',
      'TEMPORARY': 'Temporary',
      'fulltime': 'Full Time',
      'contract': 'Contract'
    };

    return jobTypeMap[jobType] || jobType;
  }

  private getDemandLevelFromCategory(category?: string): 'stable' | 'high' | 'very-high' {
    if (!category) return 'stable';

    const lowerCategory = category.toLowerCase();
    if (lowerCategory.includes('high') || lowerCategory.includes('urgent')) {
      return 'very-high';
    } else if (lowerCategory.includes('demand') || lowerCategory.includes('popular')) {
      return 'high';
    }
    return 'stable';
  }

  private getExperienceLevelFromRange(experienceRange: string): string {
    if (!experienceRange) return 'mid';

    const lowerExperience = experienceRange.toLowerCase();

    // Entry level indicators
    if (lowerExperience.includes('0') ||
        lowerExperience.includes('entry') ||
        lowerExperience.includes('junior') ||
        lowerExperience.includes('graduate') ||
        lowerExperience.includes('intern') ||
        lowerExperience.match(/^0-[12]/)) {
      return 'entry';
    }

    // Senior level indicators
    if (lowerExperience.includes('senior') ||
        lowerExperience.includes('lead') ||
        lowerExperience.includes('principal') ||
        lowerExperience.match(/[5-9]\+/) ||
        lowerExperience.match(/[6-9]-/) ||
        lowerExperience.match(/1[0-9]/)) {
      return 'senior';
    }

    // Executive level indicators
    if (lowerExperience.includes('executive') ||
        lowerExperience.includes('director') ||
        lowerExperience.includes('manager') ||
        lowerExperience.includes('head of') ||
        lowerExperience.includes('chief')) {
      return 'executive';
    }

    return 'mid';
  }

  // Pagination methods
  private updatePagination() {
    this.totalPages = Math.ceil(this.filteredJobs.length / this.itemsPerPage);
    this.currentPage = Math.min(this.currentPage, Math.max(1, this.totalPages));
    this.updatePaginatedJobs();
  }

  private updatePaginatedJobs() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedJobs = this.filteredJobs.slice(startIndex, endIndex);
  }

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePaginatedJobs();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  getPaginationArray(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;

    if (this.totalPages <= maxVisiblePages) {
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      const start = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
      const end = Math.min(this.totalPages, start + maxVisiblePages - 1);

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
    }

    return pages;
  }

  // Modal methods
  openJobModal(job: Job) {
    this.selectedJob = job;
    this.showJobModal = true;
    document.body.style.overflow = 'hidden';
  }

  closeJobModal() {
    this.showJobModal = false;
    this.selectedJob = null;
    document.body.style.overflow = 'auto';
  }

  retryLoad() {
    console.log('Retrying load...');
    this.loadJobs();
  }

  getStrokeColor(job: Job): string {
    return job.isSaved ? '#9BA3AF' : 'none';
  }

  onFiltersChange() {
    console.log('Applying filters:', this.filters);
    this.filteredJobs = this.jobs.filter(job => {
      let matches = true;

      // Search term filter
      if (this.filters.searchTerm) {
        const searchTerm = this.filters.searchTerm.toLowerCase();
        matches = matches && (
          job.title.toLowerCase().includes(searchTerm) ||
          job.company.toLowerCase().includes(searchTerm) ||
          job.description.toLowerCase().includes(searchTerm) ||
          job.requiredSkills.some(skill => skill.toLowerCase().includes(searchTerm))
        );
      }

      // Sector filter
      if (this.filters.sector) {
        const jobTitle = job.title.toLowerCase();
        const jobDescription = job.description.toLowerCase();
        const jobCategory = job.category?.toLowerCase() || '';

        switch (this.filters.sector) {
          case 'education':
            matches = matches && (
              jobTitle.includes('teacher') ||
              jobTitle.includes('professor') ||
              jobTitle.includes('education') ||
              jobCategory.includes('education')
            );
            break;
          case 'healthcare':
            matches = matches && (
              jobTitle.includes('psychologist') ||
              jobTitle.includes('health') ||
              jobTitle.includes('medical') ||
              jobTitle.includes('nurse') ||
              jobCategory.includes('health')
            );
            break;
          case 'technology':
            matches = matches && (
              jobTitle.includes('engineer') ||
              jobTitle.includes('developer') ||
              jobTitle.includes('software') ||
              jobTitle.includes('programmer') ||
              jobTitle.includes('tech') ||
              jobCategory.includes('tech')
            );
            break;
          case 'finance':
            matches = matches && (
              jobTitle.includes('finance') ||
              jobTitle.includes('accounting') ||
              jobTitle.includes('analyst') ||
              jobTitle.includes('banking') ||
              jobCategory.includes('finance')
            );
            break;
          case 'marketing':
            matches = matches && (
              jobTitle.includes('marketing') ||
              jobTitle.includes('sales') ||
              jobTitle.includes('advertising') ||
              jobCategory.includes('marketing')
            );
            break;
        }
      }

      // Level filter
      if (this.filters.level) {
        const jobExperienceLevel = this.getExperienceLevelFromRange(job.experience);
        matches = matches && jobExperienceLevel === this.filters.level;
      }

      return matches;
    });

    console.log('Filtered jobs result:', this.filteredJobs.length, 'jobs');
    this.currentPage = 1; // Reset to first page when filtering
    this.updatePagination();
  }

  clearFilters() {
    this.filters = { searchTerm: '', sector: '', level: '' };
    this.filteredJobs = [...this.jobs];
    this.currentPage = 1;
    this.updatePagination();
    console.log('Filters cleared, showing all jobs:', this.filteredJobs.length);
  }

  toggleSave(job: Job) {
    job.isSaved = !job.isSaved;
    // TODO: Call backend service to save/unsave job
  }

  toggleFavorite(job: Job) {
    job.isFavorite = !job.isFavorite;
    // TODO: Call backend service to favorite/unfavorite job
  }

  viewDetails(job: Job) {
    if (job.applyUrl) {
      window.open(job.applyUrl, '_blank');
    } else {
      console.log('View details for job:', job.id);
      this.openJobModal(job);
    }
  }

  saveJobToggle(job: Job) {
    if (!job.isSaved) {
      job.isSaved = true;
      console.log('Save job:', job.id);
      // TODO: Call backend service to save job
    } else {
      job.isSaved = false;
      console.log('Unsave job:', job.id);
      // TODO: Call backend service to unsave job
    }
  }

  getDemandText(demandLevel: string): string {
    switch (demandLevel) {
      case 'stable': return 'Stable';
      case 'high': return 'High demand';
      case 'very-high': return 'Very high demand';
      default: return 'Stable';
    }
  }

  trackByJobId(index: number, job: Job): string {
    return job.id;
  }

  // Utility methods for modal
  formatDate(dateString?: string): string {
    if (!dateString) return 'Not specified';
    try {
      return new Date(dateString).toLocaleDateString();
    } catch {
      return dateString;
    }
  }

  parseEmails(emails?: string): string[] {
    if (!emails) return [];
    return emails.split(',').map(email => email.trim()).filter(email => email.length > 0);
  }

  parseAddresses(addresses?: string): string[] {
    if (!addresses) return [];
    return addresses.split(',').map(addr => addr.trim()).filter(addr => addr.length > 0);
  }
}
