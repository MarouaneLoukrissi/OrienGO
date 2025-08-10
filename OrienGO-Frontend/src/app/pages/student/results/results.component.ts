import { Component } from '@angular/core';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrl: './results.component.css'
})
export class ResultsComponent{
  title = 'riasec-results';

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

  keyPoints = [
    {
      title: 'Primary Orientation',
      description: 'Your Realistic profile indicates a preference for practical, hands-on work oriented toward action.'
    },
    {
      title: 'Natural Skills',
      description: 'Your strengths include: tool handling, concrete activities, practical problem-solving.'
    },
    {
      title: 'Ideal Environment',
      description: 'You will thrive in environments that value your dominant traits.'
    },
    {
      title: 'Development Potential',
      description: 'Your other dimensions (I, A) offer complementary opportunities'
    }
  ];
}
