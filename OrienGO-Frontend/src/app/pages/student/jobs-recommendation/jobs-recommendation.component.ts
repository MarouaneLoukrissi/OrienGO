import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { forkJoin, catchError, of, finalize, switchMap } from 'rxjs';
import { PersonalizedJobService } from '../../../Service/personalizedJob.service';
import { PersonalizedJobResponseDto } from '../../../model/dto/PersonalizedJobResponse.dto';
import { LinkType } from '../../../model/enum/LinkType.enum';

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
  personalizedJobId?: number; // Add this to track the backend job ID
}

export interface SearchFilters {
  searchTerm: string;
  sector: string;
  level: string;
}

@Component({
  selector: 'app-jobs-recommendation',
  templateUrl: './jobs-recommendation.component.html',
  styleUrl: './jobs-recommendation.component.css'
})
export class JobsRecommendationComponent implements OnInit {
  constructor(
    private location: Location,
    private route: ActivatedRoute,
    private personalizedJobService: PersonalizedJobService
  ) {}

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

  isLoading = false;
  error: string | null = null;
  studentId = 1; // Hardcoded as requested

  // New property to determine if current user is owner or coach
  isOwner = true; // Default to owner, can be determined from route params or auth context

  ngOnInit() {
    console.log('JobsRecommendationComponent initialized');
    this.determineOwnership();
    this.loadJobs();
  }

  private determineOwnership() {
    // Check route parameters to determine if this is a coach view
    this.route.queryParams.subscribe(params => {
      // If there's a 'coach' parameter, this is a coach viewing student's jobs
      this.isOwner = !params['coach'] || params['coach'] !== 'true';
      console.log('User is owner:', this.isOwner);
    });
  }

  private loadJobs() {
    console.log('=== STARTING LOAD JOBS ===');
    this.isLoading = true;
    this.error = null;
    this.jobs = [];
    this.filteredJobs = [];
    this.paginatedJobs = [];

    // Get IDs from URL query parameters
    this.route.queryParams.subscribe(params => {
      console.log('Query params received:', params);
      const idsParam = params['ids'];

      if (idsParam) {
        // Parse comma-separated IDs
        const jobRecommendationIds = idsParam.split(',')
          .map((id: string) => parseInt(id.trim(), 10))
          .filter((id: number) => !isNaN(id));

        console.log('Parsed job recommendation IDs:', jobRecommendationIds);

        if (jobRecommendationIds.length > 0) {
          // Call appropriate endpoint based on ownership
          if (this.isOwner) {
            this.getJobsByRecommendationIds(jobRecommendationIds);
          } else {
            this.getJobsByRecommendationIdsForCoach(jobRecommendationIds);
          }
        } else {
          console.log('Invalid IDs, fallback to getByStudentId');
          this.getJobsByStudentId();
        }
      } else {
        console.log('No IDs provided, get all jobs for student');
        this.getJobsByStudentId();
      }
    });
  }

  private getJobsByRecommendationIds(jobRecommendationIds: number[]) {
    console.log('=== CALLING getByJobRecommendationIds (Owner) ===');
    console.log('IDs:', jobRecommendationIds);

    this.personalizedJobService.getByJobRecommendationIds(this.studentId, jobRecommendationIds)
      .pipe(
        catchError(error => {
          console.error('Error in getByJobRecommendationIds:', error);
          // If we get a 404, it means no jobs exist yet, so we should try scraping
          if (error.status === 404) {
            console.log('No existing jobs found (404), trying scrape...');
            return this.personalizedJobService.scrapeAndCreate(jobRecommendationIds, this.studentId)
              .pipe(
                switchMap(scrapeResponse => {
                  console.log('Scrape response:', scrapeResponse);
                  if (scrapeResponse?.data) {
                    // Flatten the scrape response data
                    const allJobs: PersonalizedJobResponseDto[] = [];
                    Object.values(scrapeResponse.data).forEach(jobArray => {
                      if (Array.isArray(jobArray)) {
                        allJobs.push(...jobArray);
                      }
                    });
                    return of({ data: allJobs });
                  }
                  return of({ data: [] });
                }),
                catchError(scrapeError => {
                  console.error('Scraping also failed:', scrapeError);
                  this.error = 'Failed to fetch and generate personalized jobs. Please try again.';
                  return of(null);
                })
              );
          }

          this.error = 'Failed to fetch personalized jobs. Please try again.';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('=== FINISHED LOADING ===');
        })
      )
      .subscribe({
        next: (response) => {
          console.log('=== RESPONSE RECEIVED ===');
          console.log('Response:', response);

          if (response?.data) {
            console.log('Processing response data...');
            this.processJobsData(response.data);
          } else {
            console.log('No data in response');
            this.jobs = [];
            this.filteredJobs = [];
            this.updatePagination();
          }
        },
        error: (err) => {
          console.error('Subscription error:', err);
          this.error = 'An unexpected error occurred';
        }
      });
  }

  private getJobsByRecommendationIdsForCoach(jobRecommendationIds: number[]) {
    console.log('=== CALLING getByJobRecommendationIdsForCoach (Coach) ===');
    console.log('IDs:', jobRecommendationIds);

    this.personalizedJobService.getByJobRecommendationIdsForCoach(jobRecommendationIds)
      .pipe(
        catchError(error => {
          console.error('Error in getByJobRecommendationIdsForCoach:', error);
          this.error = 'Failed to fetch personalized jobs. Please try again.';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('=== FINISHED LOADING FOR COACH ===');
        })
      )
      .subscribe({
        next: (response) => {
          console.log('=== COACH RESPONSE RECEIVED ===');
          console.log('Response:', response);

          if (response?.data) {
            console.log('Processing coach response data...');
            this.processJobsData(response.data);
          } else {
            console.log('No data in coach response');
            this.jobs = [];
            this.filteredJobs = [];
            this.updatePagination();
          }
        },
        error: (err) => {
          console.error('Coach subscription error:', err);
          this.error = 'An unexpected error occurred';
        }
      });
  }

  private getJobsByStudentId() {
    console.log('=== CALLING getByStudentId ===');
    console.log('Student ID:', this.studentId);

    this.personalizedJobService.getByStudentId(this.studentId)
      .pipe(
        catchError(error => {
          console.error('Error fetching jobs by student ID:', error);
          this.error = 'Failed to fetch your personalized jobs. Please try again.';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('=== FINISHED LOADING BY STUDENT ID ===');
        })
      )
      .subscribe({
        next: (response) => {
          console.log('=== STUDENT RESPONSE RECEIVED ===');
          console.log('Response:', response);

          if (response?.data) {
            this.processJobsData(response.data);
          } else {
            this.jobs = [];
            this.filteredJobs = [];
            this.updatePagination();
          }
        },
        error: (err) => {
          console.error('Student subscription error:', err);
          this.error = 'An unexpected error occurred';
        }
      });
  }

  private processJobsData(jobsData: PersonalizedJobResponseDto[]) {
    console.log('=== PROCESSING JOBS DATA ===');
    console.log('Raw jobs data:', jobsData);
    console.log('Jobs data type:', typeof jobsData);
    console.log('Is array:', Array.isArray(jobsData));
    console.log('Length:', jobsData?.length);

    if (!jobsData) {
      console.error('JobsData is null or undefined');
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
      return;
    }

    if (!Array.isArray(jobsData)) {
      console.error('JobsData is not an array:', jobsData);
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
      return;
    }

    if (jobsData.length === 0) {
      console.warn('JobsData array is empty');
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
      return;
    }

    try {
      console.log('Mapping jobs...');

      // Filter out fallback jobs before mapping
      const realJobs = jobsData.filter(dto => !this.isFallbackJob(dto));
      console.log(`Filtered out ${jobsData.length - realJobs.length} fallback jobs`);

      this.jobs = realJobs.map((dto, index) => {
        console.log(`Mapping job ${index + 1}:`, dto);
        const mappedJob = this.mapDtoToJob(dto);
        console.log(`Mapped job ${index + 1}:`, mappedJob);
        return mappedJob;
      });

      console.log('All jobs mapped:', this.jobs);
      console.log('Total jobs after mapping:', this.jobs.length);

      // Set filtered jobs to all jobs initially
      this.filteredJobs = [...this.jobs];
      console.log('Filtered jobs set:', this.filteredJobs.length);

      // Update pagination
      this.updatePagination();

      // Force change detection
      setTimeout(() => {
        console.log('=== FINAL STATE ===');
        console.log('Jobs count:', this.jobs.length);
        console.log('Filtered jobs count:', this.filteredJobs.length);
        console.log('Paginated jobs count:', this.paginatedJobs.length);
        console.log('Is loading:', this.isLoading);
        console.log('Error:', this.error);
      }, 0);

    } catch (error) {
      console.error('Error during job mapping:', error);
      this.error = 'Error processing job data';
      this.jobs = [];
      this.filteredJobs = [];
      this.updatePagination();
    }
  }

  private isFallbackJob(dto: PersonalizedJobResponseDto): boolean {
    // Check multiple criteria to identify fallback jobs
    return !!(
      dto.source === 'FALLBACK' ||
      dto.companyName === 'Fallback Company' ||
      dto.location === 'Fallback Location' ||
      (dto.title && dto.title.toLowerCase().includes('fallback job')) ||
      (dto.description && dto.description.toLowerCase().includes('fallback job created due to scraping failure'))
    );
  }

  private mapDtoToJob(dto: PersonalizedJobResponseDto): Job {
    console.log('Mapping DTO:', dto.id, dto.title);

    // Safely parse required skills
    let requiredSkills: string[] = [];
    try {
      if (dto.requiredSkills) {
        // requiredSkills is always a string in the DTO
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
        // advantages is always an array in the DTO
        advantages = dto.advantages.filter((adv: string) => adv && adv.length > 0);
      }
    } catch (error) {
      console.warn('Error parsing advantages:', error);
      advantages = [];
    }

    // Determine save and favorite status from linkTypes
    const linkTypes = dto.linkTypes || [];
    const isSaved = linkTypes.includes(LinkType.SAVED);
    const isFavorite = linkTypes.includes(LinkType.FAVORITE);

    const mappedJob: Job = {
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
      isSaved: isSaved,
      isFavorite: isFavorite,
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
      category: dto.category,
      personalizedJobId: dto.id // Store the backend ID for API calls
    };

    console.log('Mapped job result:', mappedJob.id, mappedJob.title, 'isSaved:', mappedJob.isSaved, 'isFavorite:', mappedJob.isFavorite);
    return mappedJob;
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

    // Convert common job types to readable format
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

    // Mid level (default for 2-5 years experience)
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
      // Scroll to top
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  getPaginationArray(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;

    if (this.totalPages <= maxVisiblePages) {
      // Show all pages if total pages is less than or equal to maxVisiblePages
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      // Show pages around current page
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
    document.body.style.overflow = 'hidden'; // Prevent background scrolling
  }

  closeJobModal() {
    this.showJobModal = false;
    this.selectedJob = null;
    document.body.style.overflow = 'auto'; // Restore scrolling
  }

  getStrokeColor(job: Job): string {
    return job.isSaved ? '#9BA3AF' : 'none';
  }

  goBack() {
    this.location.back();
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
    // Only allow save/unsave if user is the owner
    if (!this.isOwner) {
      console.log('Coach cannot save/unsave jobs');
      return;
    }

    if (!job.personalizedJobId) {
      console.error('No personalized job ID available for save operation');
      return;
    }

    const currentlySaved = job.isSaved;
    const linkType = LinkType.SAVED;

    if (currentlySaved) {
      // Remove save link using the unlink endpoint
      this.removeJobLink(job, linkType);
    } else {
      // Add save link
      this.addJobLink(job, linkType);
    }
  }

  toggleFavorite(job: Job) {
    // Only allow favorite/unfavorite if user is the owner
    if (!this.isOwner) {
      console.log('Coach cannot favorite/unfavorite jobs');
      return;
    }

    if (!job.personalizedJobId) {
      console.error('No personalized job ID available for favorite operation');
      return;
    }

    const currentlyFavorite = job.isFavorite;
    const linkType = LinkType.FAVORITE;

    if (currentlyFavorite) {
      // Remove favorite link using the unlink endpoint
      this.removeJobLink(job, linkType);
    } else {
      // Add favorite link
      this.addJobLink(job, linkType);
    }
  }

  private addJobLink(job: Job, linkType: LinkType) {
    console.log(`Adding ${linkType} link for job:`, job.id);

    // Optimistically update UI
    if (linkType === LinkType.SAVED) {
      job.isSaved = true;
    } else if (linkType === LinkType.FAVORITE) {
      job.isFavorite = true;
    }

    this.personalizedJobService.linkJobToStudent(
      this.studentId,
      job.personalizedJobId!,
      linkType
    ).pipe(
      catchError(error => {
        console.error(`Error adding ${linkType} link:`, error);
        // Revert the UI change on error
        if (linkType === LinkType.SAVED) {
          job.isSaved = false;
        } else if (linkType === LinkType.FAVORITE) {
          job.isFavorite = false;
        }
        return of(null);
      })
    ).subscribe({
      next: (response) => {
        if (response) {
          console.log(`Successfully added ${linkType} link:`, response);
        }
      },
      error: (err) => {
        console.error(`Failed to add ${linkType} link:`, err);
      }
    });
  }

  private removeJobLink(job: Job, linkType: LinkType) {
    console.log(`Removing ${linkType} link for job:`, job.id);

    // Optimistically update UI
    if (linkType === LinkType.SAVED) {
      job.isSaved = false;
    } else if (linkType === LinkType.FAVORITE) {
      job.isFavorite = false;
    }

    // Use the proper unlink endpoint
    this.personalizedJobService.unlinkJobFromStudent(
      this.studentId,
      job.personalizedJobId!,
      linkType
    ).pipe(
      catchError(error => {
        console.error(`Error removing ${linkType} link:`, error);
        // Revert the UI change on error
        if (linkType === LinkType.SAVED) {
          job.isSaved = true;
        } else if (linkType === LinkType.FAVORITE) {
          job.isFavorite = true;
        }
        return of(null);
      })
    ).subscribe({
      next: (response) => {
        if (response) {
          console.log(`Successfully removed ${linkType} link:`, response);
        }
      },
      error: (err) => {
        console.error(`Failed to remove ${linkType} link:`, err);
      }
    });
  }

  viewDetails(job: Job) {
    // Navigate to job details or open apply URL
    if (job.applyUrl) {
      window.open(job.applyUrl, '_blank');
    } else {
      console.log('View details for job:', job.id);
      // TODO: Implement job details modal or navigation
    }
  }

  saveJobToggle(job: Job) {
    this.toggleSave(job);
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

  retryLoad() {
    console.log('Retrying load...');
    this.loadJobs();
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

  // Check if current user can perform save/favorite actions
  canPerformActions(): boolean {
    return this.isOwner;
  }
}
