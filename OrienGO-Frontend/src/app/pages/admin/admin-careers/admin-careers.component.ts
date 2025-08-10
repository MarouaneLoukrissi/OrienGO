import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

// Enum correspondant à l'entité backend
enum JobCategory {
  REALISTIC = 'REALISTIC',
  INVESTIGATIVE = 'INVESTIGATIVE',
  ARTISTIC = 'ARTISTIC',
  SOCIAL = 'SOCIAL',
  ENTERPRISING = 'ENTERPRISING',
  CONVENTIONAL = 'CONVENTIONAL'
}

// Interface correspondant exactement à l'entité backend
interface Job {
  id: number;
  title: string;
  description: string;
  category: JobCategory;
  education: string;
  salaryRange: string;
  jobMarket: string;
  tags: string[];
  softDeleted: boolean;
  version: number;
}

@Component({
  selector: 'app-admin-careers',
  templateUrl: './admin-careers.component.html',
  styleUrls: ['./admin-careers.component.css']
})
export class AdminCareersComponent implements OnInit {
  // Propriétés pour les modales
  showAddJobModal = false;
  showViewJobModal = false;
  showEditJobModal = false;
  selectedJob: Job | null = null;
  editingJob: Job | null = null;

  // Formulaires
  addJobForm: FormGroup;
  editJobForm: FormGroup;

  // Propriétés pour la recherche et le tri
  searchTerm = '';
  selectedCategory = '';
  selectedJobMarket = '';
  sortField = '';
  sortDirection = 'asc';

  // Données
  originalJobs: Job[] = [
    {
      id: 1,
      title: 'Software Developer',
      description: 'Develop and maintain web applications using modern technologies. Work in agile environment.',
      category: JobCategory.REALISTIC,
      education: "Bachelor's in Computer Science",
      salaryRange: '15,000 - 25,000 MAD',
      jobMarket: 'High demand',
      tags: ['JavaScript', 'React', 'Node.js', 'Problem Solving'],
      softDeleted: false,
      version: 1
    },
    {
      id: 2,
      title: 'Clinical Psychologist',
      description: 'Psychological support for patients and their families. Work in a multidisciplinary team.',
      category: JobCategory.SOCIAL,
      education: "Master's in Clinical Psychology",
      salaryRange: '12,000 - 20,000 MAD',
      jobMarket: 'Stable',
      tags: ['Clinical Psychology', 'Listening', 'Empathy', 'Therapy'],
      softDeleted: false,
      version: 1
    },
    {
      id: 3,
      title: 'Marketing Manager',
      description: 'Lead marketing campaigns and strategies for various clients. Manage team and budgets.',
      category: JobCategory.ENTERPRISING,
      education: "Bachelor's in Marketing",
      salaryRange: '18,000 - 30,000 MAD',
      jobMarket: 'High demand',
      tags: ['Digital Marketing', 'Strategy', 'Leadership', 'Analytics'],
      softDeleted: false,
      version: 1
    },
    {
      id: 4,
      title: 'Data Analyst',
      description: 'Analyze data to help organizations make informed decisions. Create reports and visualizations.',
      category: JobCategory.INVESTIGATIVE,
      education: "Bachelor's in Statistics or Mathematics",
      salaryRange: '12,000 - 20,000 MAD',
      jobMarket: 'High demand',
      tags: ['Data Analysis', 'SQL', 'Python', 'Statistics'],
      softDeleted: false,
      version: 1
    },
    {
      id: 5,
      title: 'Graphic Designer',
      description: 'Create visual content for brands and organizations. Work with design software and creative tools.',
      category: JobCategory.ARTISTIC,
      education: "Bachelor's in Graphic Design",
      salaryRange: '8,000 - 15,000 MAD',
      jobMarket: 'Stable',
      tags: ['Adobe Creative Suite', 'Creativity', 'Typography', 'Branding'],
      softDeleted: false,
      version: 1
    },
    {
      id: 6,
      title: 'Project Manager',
      description: 'Lead project teams and ensure successful delivery of projects within scope, time, and budget.',
      category: JobCategory.ENTERPRISING,
      education: "Bachelor's in Business Administration",
      salaryRange: '20,000 - 35,000 MAD',
      jobMarket: 'High demand',
      tags: ['Project Management', 'Leadership', 'Communication', 'Agile'],
      softDeleted: false,
      version: 1
    },
    {
      id: 7,
      title: 'Content Writer',
      description: 'Create engaging content for websites, blogs, and marketing materials.',
      category: JobCategory.ARTISTIC,
      education: "Bachelor's in Communications or Journalism",
      salaryRange: '6,000 - 12,000 MAD',
      jobMarket: 'Stable',
      tags: ['Content Writing', 'SEO', 'Creativity', 'Research'],
      softDeleted: false,
      version: 1
    },
    {
      id: 8,
      title: 'UX/UI Designer',
      description: 'Design user interfaces and experiences for digital products. Focus on usability and user satisfaction.',
      category: JobCategory.ARTISTIC,
      education: "Bachelor's in Design or related field",
      salaryRange: '14,000 - 22,000 MAD',
      jobMarket: 'High demand',
      tags: ['UI/UX Design', 'Figma', 'User Research', 'Prototyping'],
      softDeleted: false,
      version: 1
    }
  ];

  // Liste des jobs (filtrée et triée)
  jobs: Job[] = [];

  // Arrays pour les enums
  jobCategories = Object.values(JobCategory);

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService
  ) {
    this.addJobForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      category: [null, Validators.required],
      education: ['', [Validators.required, Validators.maxLength(100)]],
      salaryRange: ['', [Validators.required, Validators.maxLength(100)]],
      jobMarket: ['', [Validators.required, Validators.maxLength(100)]],
      tags: ['', [Validators.required, Validators.maxLength(500)]]
    });

    this.editJobForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      category: [null, Validators.required],
      education: ['', [Validators.required, Validators.maxLength(100)]],
      salaryRange: ['', [Validators.required, Validators.maxLength(100)]],
      jobMarket: ['', [Validators.required, Validators.maxLength(100)]],
      tags: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.initializeJobs();
  }

  // Méthodes pour les modales
  openAddJobModal(): void {
    this.showAddJobModal = true;
    this.addJobForm.reset();
  }

  closeAddJobModal(): void {
    this.showAddJobModal = false;
    this.addJobForm.reset();
  }

  onSubmit(): void {
    if (this.addJobForm.valid) {
      const formValue = this.addJobForm.value;
      const newJob: Job = {
        id: this.jobs.length + 1,
        title: formValue.title,
        description: formValue.description,
        category: formValue.category,
        education: formValue.education,
        salaryRange: formValue.salaryRange,
        jobMarket: formValue.jobMarket,
        tags: formValue.tags.split(',').map((tag: string) => tag.trim()),
        softDeleted: false,
        version: 1
      };

      this.jobs.push(newJob);
      this.originalJobs.push(newJob);
      this.closeAddJobModal();
      this.applyFilters();
    } else {
      this.markFormGroupTouched(this.addJobForm);
    }
  }

  openViewJobModal(job: Job): void {
    this.selectedJob = job;
    this.showViewJobModal = true;
  }

  closeViewJobModal(): void {
    this.showViewJobModal = false;
    this.selectedJob = null;
  }

  openEditJobModal(job: Job): void {
    this.editingJob = { ...job };
    this.editJobForm.patchValue({
      title: job.title,
      description: job.description,
      category: job.category,
      education: job.education,
      salaryRange: job.salaryRange,
      jobMarket: job.jobMarket,
      tags: job.tags.join(', ')
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
      const formValue = this.editJobForm.value;
      const index = this.jobs.findIndex(job => job.id === this.editingJob!.id);
      
      if (index !== -1) {
        this.jobs[index] = {
          ...this.jobs[index],
          title: formValue.title,
          description: formValue.description,
          category: formValue.category,
          education: formValue.education,
          salaryRange: formValue.salaryRange,
          jobMarket: formValue.jobMarket,
          tags: formValue.tags.split(',').map((tag: string) => tag.trim()),
          version: this.jobs[index].version + 1
        };

        // Mettre à jour aussi dans originalJobs
        const originalIndex = this.originalJobs.findIndex(job => job.id === this.editingJob!.id);
        if (originalIndex !== -1) {
          this.originalJobs[originalIndex] = { ...this.jobs[index] };
        }
      }

      this.closeEditJobModal();
      this.applyFilters();
    } else {
      this.markFormGroupTouched(this.editJobForm);
    }
  }

  deleteJob(job: Job): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce poste ?')) {
      this.jobs = this.jobs.filter(j => j.id !== job.id);
      this.originalJobs = this.originalJobs.filter(j => j.id !== job.id);
      this.applyFilters();
    }
  }

  // Méthodes de validation
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
        return this.translate.instant('ADMIN_CAREERS.MODAL.REQUIRED_FIELD');
      }
      if (control.errors['minlength']) {
        return this.translate.instant('ADMIN_CAREERS.MODAL.MIN_LENGTH', { min: control.errors['minlength'].requiredLength });
      }
      if (control.errors['maxlength']) {
        return this.translate.instant('ADMIN_CAREERS.MODAL.MAX_LENGTH', { max: control.errors['maxlength'].requiredLength });
      }
    }
    return '';
  }

  // Méthodes pour les labels des enums
  getJobCategoryLabel(category: JobCategory): string {
    try {
      return this.translate.instant(`ADMIN_CAREERS.JOB_CATEGORY.${category}`);
    } catch {
      return category;
    }
  }

  // Méthodes de recherche et tri
  onSearchChange(): void {
    this.applyFilters();
  }

  onCategoryChange(): void {
    this.applyFilters();
  }

  onJobMarketChange(): void {
    this.applyFilters();
  }

  onSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilters();
  }

  applyFilters(): void {
    let filteredJobs = [...this.originalJobs];

    // Filtre par recherche
    if (this.searchTerm) {
      const searchLower = this.searchTerm.toLowerCase();
      filteredJobs = filteredJobs.filter(job =>
        job.title.toLowerCase().includes(searchLower) ||
        job.description.toLowerCase().includes(searchLower) ||
        job.education.toLowerCase().includes(searchLower)
      );
    }

    // Filtre par catégorie
    if (this.selectedCategory) {
      filteredJobs = filteredJobs.filter(job => job.category === this.selectedCategory);
    }

    // Filtre par marché du travail
    if (this.selectedJobMarket) {
      filteredJobs = filteredJobs.filter(job => job.jobMarket === this.selectedJobMarket);
    }

    // Tri
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
  }

  initializeJobs(): void {
    this.jobs = [...this.originalJobs];
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.selectedJobMarket = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.applyFilters();
  }

  getUniqueJobMarkets(): string[] {
    return [...new Set(this.originalJobs.map(job => job.jobMarket))];
  }
}
