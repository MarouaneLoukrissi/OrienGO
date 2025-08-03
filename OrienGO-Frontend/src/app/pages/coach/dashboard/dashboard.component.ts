import { Component, AfterViewInit } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements AfterViewInit {
  radarData = {
    R: 90,
    I: 35,
    A: 55,
    S: 78,
    E: 55,
    C: 45
  };

  barData = {
    R: 100,
    I: 35,
    A: 55,
    S: 78,
    E: 55,
    C: 45
  };
  detailedScores = [
    {
      label: 'Realistic (R)',
      percentage: 44,
      color: '#3b82f6',
      description: 'Practical, hands-on, action-oriented',
      tags: ['Tool handling', 'Concrete activities', 'Practical problem-solving']
    },
    {
      label: 'Investigative (I)',
      percentage: 44,
      color: '#8b5cf6',
      description: 'Analytical, curious, scientific',
      tags: ['Research', 'Analysis', 'Experimentation']
    },
    {
      label: 'Artistic (A)',
      percentage: 44,
      color: '#ec4899',
      description: 'Creative, expressive, original',
      tags: ['Creativity', 'Artistic expression', 'Innovation']
    },
    {
      label: 'Social (S)',
      percentage: 33,
      color: '#10b981',
      description: 'Altruistic, cooperative, kind',
      tags: ['Helping others', 'Communication', 'Teaching']
    },
    {
      label: 'Enterprising (E)',
      percentage: 33,
      color: '#ef4444',
      description: 'Persuasive, ambitious, energetic',
      tags: ['Leadership', 'Persuasion', 'Risk-taking']
    },
    {
      label: 'Conventional (C)',
      percentage: 22,
      color: '#6366f1',
      description: 'Organized, precise, methodical',
      tags: ['Organization', 'Precision', 'Procedures']
    }
  ];

  ngAfterViewInit(): void {
    new Chart('radarChart', {
      type: 'radar',
      data: {
        labels: ['R', 'I', 'A', 'S', 'E', 'C'],
        datasets: [{
          label: 'Profile',
          data: [44, 41, 39, 36, 33, 22],
          backgroundColor: 'rgba(253, 186, 116, 0.2)',
          borderColor: '#fb923c',
        }]
      }
    });

    new Chart('barChart', {
      type: 'bar',
      data: {
        labels: ['R', 'I', 'A', 'S', 'E', 'C'],
        datasets: [{
          label: 'Score',
          data: [44, 41, 39, 36, 33, 22],
          backgroundColor: ['#fb923c', '#f87171', '#34d399', '#60a5fa', '#a78bfa', '#fcd34d']
        }]
      }
    });
  }
}
