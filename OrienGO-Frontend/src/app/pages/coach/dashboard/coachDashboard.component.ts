import { Component, OnInit } from '@angular/core';
import { TestResultProfilesDTO } from '../../../model/dto/TestResultProfiles.dto';
import { Category } from '../../../model/enum/Category.enum';
import { CoachStudentConnectionService } from '../../../Service/CoachStudentConnection.service';

@Component({
  selector: 'app-coach-dashboard',
  templateUrl: './coachDashboard.component.html',
})
export class CoachDashboardComponent implements OnInit {
  // Current coach ID (hardcoded for now as requested)
  currentCoachId = 3;
  
  // Loading and error states
  isLoading = true;
  hasError = false;
  errorMessage = '';

  // Chart data
  radarData: { [key: string]: number } = {};
  barData: { [key: string]: number } = {};
  
  // Detailed scores data
  detailedScores: Array<{
    label: string;
    percentage: number;
    color: string;
    description: string;
    tags: string[];
  }> = [];

  // Dominant profile data
  dominantProfile: {
    name: string;
    percentage: number;
    category: string;
  } = {
    name: '',
    percentage: 0,
    category: ''
  };

  // Static data for profile descriptions and colors
  private profileDescriptions: { [key: string]: { description: string; tags: string[] } } = {
    REALISTIC: {
      description: 'Practical, hands-on, action-oriented',
      tags: ['Tool handling', 'Concrete activities', 'Practical problem-solving']
    },
    INVESTIGATIVE: {
      description: 'Analytical, curious, scientific',
      tags: ['Research', 'Analysis', 'Experimentation']
    },
    ARTISTIC: {
      description: 'Creative, expressive, original',
      tags: ['Creativity', 'Artistic expression', 'Innovation']
    },
    SOCIAL: {
      description: 'Altruistic, cooperative, kind',
      tags: ['Helping others', 'Communication', 'Teaching']
    },
    ENTERPRISING: {
      description: 'Persuasive, ambitious, energetic',
      tags: ['Leadership', 'Persuasion', 'Risk-taking']
    },
    CONVENTIONAL: {
      description: 'Organized, precise, methodical',
      tags: ['Organization', 'Precision', 'Procedures']
    }
  };

  private profileColors: { [key: string]: string } = {
    REALISTIC: '#3b82f6',
    INVESTIGATIVE: '#8b5cf6',
    ARTISTIC: '#ec4899',
    SOCIAL: '#10b981',
    ENTERPRISING: '#ef4444',
    CONVENTIONAL: '#6366f1'
  };

  private profileNames: { [key: string]: string } = {
    REALISTIC: 'Realistic',
    INVESTIGATIVE: 'Investigative',
    ARTISTIC: 'Artistic',
    SOCIAL: 'Social',
    ENTERPRISING: 'Enterprising',
    CONVENTIONAL: 'Conventional'
  };

  // Mapping from full enum names to single letters for charts
  private categoryToLetter: { [key: string]: string } = {
    REALISTIC: 'R',
    INVESTIGATIVE: 'I',
    ARTISTIC: 'A',
    SOCIAL: 'S',
    ENTERPRISING: 'E',
    CONVENTIONAL: 'C'
  };

  constructor(private coachStudentConnectionService: CoachStudentConnectionService) {}

  ngOnInit(): void {
    this.loadCoacheesAverageProfiles();
  }

  private loadCoacheesAverageProfiles(): void {
    this.isLoading = true;
    this.hasError = false;

    // Using 'ACCEPTED' status to get profiles of accepted students only
    this.coachStudentConnectionService.getCoacheesAverageProfiles(this.currentCoachId, 'ACCEPTED').subscribe({
      next: (response) => {
        if (response.status === 200 && response.data) {
          this.transformProfileData(response.data);
        } else {
          this.handleError('Invalid response from server');
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading coachees average profiles:', error);
        this.handleError('Failed to load coachees profiles. Please try again.');
        this.isLoading = false;
      }
    });
  }

  private transformProfileData(profilesData: TestResultProfilesDTO): void {
    // Transform profiles array to objects for charts
    this.radarData = {};
    this.barData = {};
    this.detailedScores = [];

    let maxPercentage = 0;
    let dominantCategory = '';

    profilesData.profiles.forEach(profile => {
      const categoryKey = this.getCategoryKey(profile.category);
      const letterKey = this.categoryToLetter[categoryKey];
      const percentage = Math.round(profile.percentage * 10) / 10;
      
      // Set chart data using letter keys
      this.radarData[letterKey] = percentage;
      this.barData[letterKey] = percentage;

      // Find dominant profile
      if (percentage > maxPercentage) {
        maxPercentage = percentage;
        dominantCategory = categoryKey;
      }

      // Build detailed scores
      this.detailedScores.push({
        label: `${this.profileNames[categoryKey]} (${letterKey})`,
        percentage: percentage,
        color: this.profileColors[categoryKey],
        description: this.profileDescriptions[categoryKey].description,
        tags: this.profileDescriptions[categoryKey].tags
      });
    });

    // Sort detailed scores by percentage (highest first)
    this.detailedScores.sort((a, b) => b.percentage - a.percentage);

    // Set dominant profile
    this.dominantProfile = {
      name: this.profileNames[dominantCategory],
      percentage: maxPercentage,
      category: dominantCategory
    };
  }

  private getCategoryKey(category: Category): string {
    // Return the enum value as string
    if (typeof category === 'string') {
      return category;
    }
    // If it's an enum object, get the string value
    return category;
  }

  private handleError(message: string): void {
    this.hasError = true;
    this.errorMessage = message;
    
    // Set empty data to prevent chart errors
    this.radarData = { R: 0, I: 0, A: 0, S: 0, E: 0, C: 0 };
    this.barData = { R: 0, I: 0, A: 0, S: 0, E: 0, C: 0 };
    this.detailedScores = [];
    this.dominantProfile = { name: 'Not Available', percentage: 0, category: '' };
  }

  // Method to retry loading data
  retryLoading(): void {
    this.loadCoacheesAverageProfiles();
  }
}