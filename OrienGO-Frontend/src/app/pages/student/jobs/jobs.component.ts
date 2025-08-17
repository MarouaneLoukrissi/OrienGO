import { Component } from '@angular/core';

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
export class JobsComponent {
  getStrokeColor(job: Job): string {
    return job.isSaved ? '#9BA3AF' : 'none';
  }
  jobs: Job[] = [
    {
      id: '1',
      title: 'Mathematics Teacher',
      company: 'National Education',
      location: 'Toulouse, France',
      description: 'Teaching mathematics in high school. Knowledge transmission and student support.',
      salaryRange: '25,000 - 40,000',
      experience: '0-2',
      employmentType: 'Public servant',
      demandLevel: 'stable',
      matchPercentage: 63,
      requiredSkills: ['Pedagogy', 'Mathematics', 'Communication', 'Patience'],
      advantages: ['Job security', 'School holidays', 'Training', 'Educational mission'],
      isSaved: false,
      isFavorite: false
    },
    {
      id: '2',
      title: 'Clinical Psychologist',
      company: 'Saint-Louis Hospital',
      location: 'Paris, France',
      description: 'Psychological support for patients and their families. Work in a multidisciplinary team.',
      salaryRange: '30,000 - 45,000',
      experience: '1-3',
      employmentType: 'Permanent contract (CDI)',
      demandLevel: 'high',
      matchPercentage: 58,
      requiredSkills: ['Clinical Psychology', 'Listening', 'Empathy', 'Therapy'],
      advantages: ['Sense of service', 'Continuing education', 'Medical team', 'Social impact'],
      isSaved: true,
      isFavorite: false
    },
    {
      id: '3',
      title: 'Software Engineer',
      company: 'Tech Solutions',
      location: 'Lyon, France',
      description: 'Develop and maintain web applications using modern technologies. Work in agile environment.',
      salaryRange: '35,000 - 55,000',
      experience: '2-5',
      employmentType: 'Permanent contract (CDI)',
      demandLevel: 'very-high',
      matchPercentage: 72,
      requiredSkills: ['JavaScript', 'React', 'Node.js', 'Problem Solving'],
      advantages: ['Remote work', 'Growth opportunities', 'Tech stack', 'Innovation'],
      isSaved: false,
      isFavorite: true
    },
    {
      id: '4',
      title: 'Marketing Manager',
      company: 'Digital Agency',
      location: 'Marseille, France',
      description: 'Lead marketing campaigns and strategies for various clients. Manage team and budgets.',
      salaryRange: '40,000 - 60,000',
      experience: '3-6',
      employmentType: 'Permanent contract (CDI)',
      demandLevel: 'high',
      matchPercentage: 45,
      requiredSkills: ['Digital Marketing', 'Strategy', 'Leadership', 'Analytics'],
      advantages: ['Creative freedom', 'Client diversity', 'Team management', 'Results driven'],
      isSaved: false,
      isFavorite: true
    }
  ];

  filteredJobs: Job[] = [];
  filters: SearchFilters = {
    searchTerm: '',
    sector: '',
    level: ''
  };

  ngOnInit() {
    this.filteredJobs = [...this.jobs];
  }

  onFiltersChange() {
    this.filteredJobs = this.jobs.filter(job => {
      let matches = true;

      // Search term filter
      if (this.filters.searchTerm) {
        const searchTerm = this.filters.searchTerm.toLowerCase();
        matches = matches && (
          job.title.toLowerCase().includes(searchTerm) ||
          job.company.toLowerCase().includes(searchTerm) ||
          job.description.toLowerCase().includes(searchTerm)
        );
      }

      // Sector filter
      if (this.filters.sector) {
        if (this.filters.sector === 'education') matches = matches && job.title.includes('Teacher');
        if (this.filters.sector === 'healthcare') matches = matches && job.title.includes('Psychologist');
        if (this.filters.sector === 'technology') matches = matches && job.title.includes('Engineer');
        if (this.filters.sector === 'marketing') matches = matches && job.title.includes('Marketing');
      }

      return matches;
    });
  }

  clearFilters() {
    this.filters = { searchTerm: '', sector: '', level: '' };
    this.filteredJobs = [...this.jobs];
  }

  toggleSave(job: Job) {
    job.isSaved = !job.isSaved;
    // Here you would typically call a service to update the backend
  }
  toggleFavorite(job: Job) {
    job.isFavorite = !job.isFavorite;
    // Here you would typically call a service to update the backend
  }

  viewDetails(job: Job) {
    // Navigate to job details or open modal
    console.log('View details for job:', job.id);
  }

  saveJobToggle(job: Job) {
    if(!job.isSaved){
      job.isSaved = true;
      // Here you would typically call a service to save to backend
      console.log('Save job:', job.id);
    }else{
      job.isSaved = false;
      // Here you would typically call a service to Unsave to backend
      console.log('Unsave job:', job.id);
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
}
