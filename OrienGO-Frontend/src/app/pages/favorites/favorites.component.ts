import { Component } from '@angular/core';

interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  salary: string;
  addedOn: string;
  matchPercentage: number;
}

interface Training {
  id: string;
  title: string;
  institution: string;
  duration: string;
  degree: string;
  addedOn: string;
  matchPercentage: number;
}

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.css'
})

export class FavoritesComponent {
  favoriteJobs: Job[] = [
    {
      id: '1',
      title: 'Data Science Engineer',
      company: 'Google',
      location: 'Paris',
      salary: '60 000 - 80 000$',
      addedOn: '15/01/2024',
      matchPercentage: 92
    },
    {
      id: '2',
      title: 'IT Project Manager',
      company: 'Microsoft',
      location: 'Toulouse',
      salary: '55 000 - 75 000$',
      addedOn: '01/08/2024',
      matchPercentage: 85
    },
    {
      id: '3',
      title: 'Full Stack Developer',
      company: 'Spotify',
      location: 'Lyon',
      salary: '45 000 - 65 000$',
      addedOn: '01/10/2024',
      matchPercentage: 88
    }
  ];

  favoriteTrainings: Training[] = [
    {
      id: '1',
      title: 'Master in Data Science',
      institution: 'École Polytechnique',
      duration: '2 years',
      degree: "Master's degree (5 years post high school)",
      addedOn: '01/12/2024',
      matchPercentage: 90
    },
    {
      id: '2',
      title: 'Computer Engineering School',
      institution: 'EPITECH',
      duration: '5 years',
      degree: "Master's degree (5 years post high school)",
      addedOn: '01/05/2024',
      matchPercentage: 87
    }
  ];

  // Methods ready for backend integration
  viewJobDetails(jobId: string): void {
    console.log('View job details:', jobId);
    // TODO: Navigate to job details or call API
  }

  toggleJobVisibility(jobId: string): void {
    console.log('Toggle job visibility:', jobId);
    // TODO: Call API to toggle job visibility
  }

  viewTrainingDetails(trainingId: string): void {
    console.log('View training details:', trainingId);
    // TODO: Navigate to training details or call API
  }

  toggleTrainingVisibility(trainingId: string): void {
    console.log('Toggle training visibility:', trainingId);
    // TODO: Call API to toggle training visibility
  }
}
