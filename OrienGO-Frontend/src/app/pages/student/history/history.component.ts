import { Component } from '@angular/core';

interface TestResult {
  id: string;
  type: string;
  date: string;
  duration: number;
  dominantProfile: string;
  dominantScore: number;
  scores: {
    R: number;
    I: number;
    A: number;
    S: number;
    E: number;
    C: number;
  };
  isCurrent?: boolean;
}

interface StatCard {
  title: string;
  value: string;
  subtitle: string;
  color: string;
}

interface ChartData {
  date: string;
  profile: string;
  score: number;
  color: string;
}

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent {
  statsCards: StatCard[] = [
    { title: 'Tests passed', value: '4', subtitle: 'Tests passed', color: 'text-orange-500' },
    { title: 'Current profile', value: 'Investigator', subtitle: 'Current profile', color: 'text-orange-500' },
    { title: 'Current score', value: '85%', subtitle: 'Current score', color: 'text-orange-500' },
    { title: 'Progress', value: '+5%', subtitle: 'Tests passed', color: 'text-green-500' }
  ];

  filters = [
    { id: 'all', label: 'All tests', active: true },
    { id: '3months', label: 'Last 3 months', active: false },
    { id: 'full', label: 'Full tests', active: false },
    { id: 'express', label: 'Express tests', active: false }
  ];

  allTestResults: TestResult[] = [
    {
      id: '1',
      type: 'RIASEC Full',
      date: '15/01/2024',
      duration: 25,
      dominantProfile: 'Investigator',
      dominantScore: 85,
      scores: { R: 75, I: 85, A: 45, S: 60, E: 70, C: 55 },
      isCurrent: true
    },
    {
      id: '2',
      type: 'RIASEC Express',
      date: '10/12/2023',
      duration: 15,
      dominantProfile: 'Investigator',
      dominantScore: 80,
      scores: { R: 70, I: 80, A: 50, S: 65, E: 70, C: 55 }
    },
    {
      id: '3',
      type: 'RIASEC Full',
      date: '05/11/2023',
      duration: 25,
      dominantProfile: 'Realistic',
      dominantScore: 78,
      scores: { R: 78, I: 72, A: 48, S: 62, E: 68, C: 52 }
    },
    {
      id: '4',
      type: 'RIASEC Express',
      date: '20/10/2023',
      duration: 15,
      dominantProfile: 'Enterprising',
      dominantScore: 75,
      scores: { R: 65, I: 70, A: 55, S: 60, E: 75, C: 58 }
    }
  ];

  chartData: ChartData[] = [
    { date: '20/10/23', profile: 'E', score: 75, color: '#f97316' },
    { date: '05/11/23', profile: 'R', score: 78, color: '#ef4444' },
    { date: '10/12/23', profile: 'I', score: 80, color: '#3b82f6' },
    { date: '15/01/24', profile: 'I', score: 85, color: '#3b82f6' }
  ];

  // Chart data for different filters
  allTestsChartData: ChartData[] = [
    { date: '20/10/23', profile: 'E', score: 75, color: '#f97316' },
    { date: '05/11/23', profile: 'R', score: 78, color: '#ef4444' },
    { date: '10/12/23', profile: 'I', score: 80, color: '#3b82f6' },
    { date: '15/01/24', profile: 'I', score: 85, color: '#3b82f6' }
  ];

  last3MonthsChartData: ChartData[] = [
    { date: '10/12/23', profile: 'I', score: 80, color: '#3b82f6' },
    { date: '15/01/24', profile: 'I', score: 85, color: '#3b82f6' }
  ];

  fullTestsChartData: ChartData[] = [
    { date: '05/11/23', profile: 'R', score: 78, color: '#ef4444' },
    { date: '15/01/24', profile: 'I', score: 85, color: '#3b82f6' }
  ];

  expressTestsChartData: ChartData[] = [
    { date: '20/10/23', profile: 'E', score: 75, color: '#f97316' },
    { date: '10/12/23', profile: 'I', score: 80, color: '#3b82f6' }
  ];

  profileColors = [
    { name: 'Realistic (R)', short: 'R', color: '#3b82f6' },
    { name: 'Investigator (I)', short: 'I', color: '#8b5cf6' },
    { name: 'Artistic (A)', short: 'A', color: '#ec4899' },
    { name: 'Social (S)', short: 'S', color: '#10b981' },
    { name: 'Enterprising (E)', short: 'E', color: '#ef4444' },
    { name: 'Conventional (C)', short: 'C', color: '#6366f1' }
  ];

  getRiasecKeys(): (keyof TestResult['scores'])[] {
    return ['R', 'I', 'A', 'S', 'E', 'C'];
  }

  setActiveFilter(filterId: string): void {
    this.filters.forEach(filter => {
      filter.active = filter.id === filterId;
    });
    // Update chart data based on selected filter
    this.updateChartData();
  }

  updateChartData(): void {
    const activeFilter = this.filters.find(f => f.active);
    if (!activeFilter) return;

    switch (activeFilter.id) {
      case 'all':
        this.chartData = this.allTestsChartData;
        break;
      case '3months':
        this.chartData = this.last3MonthsChartData;
        break;
      case 'full':
        this.chartData = this.fullTestsChartData;
        break;
      case 'express':
        this.chartData = this.expressTestsChartData;
        break;
      default:
        this.chartData = this.allTestsChartData;
    }
  }

  getFilteredTests(): TestResult[] {
    const activeFilter = this.filters.find(f => f.active);
    if (!activeFilter) return this.allTestResults;

    switch (activeFilter.id) {
      case 'all':
        return this.allTestResults;
      case '3months':
        // Filter tests from last 3 months
        const threeMonthsAgo = new Date();
        threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
        return this.allTestResults.filter(test => {
          const testDate = this.parseDate(test.date);
          return testDate >= threeMonthsAgo;
        });
      case 'full':
        return this.allTestResults.filter(test => test.type.includes('Full'));
      case 'express':
        return this.allTestResults.filter(test => test.type.includes('Express'));
      default:
        return this.allTestResults;
    }
  }

  private parseDate(dateString: string): Date {
    // Parse DD/MM/YYYY format
    const [day, month, year] = dateString.split('/').map(Number);
    return new Date(year, month - 1, day);
  }

  getShortProfile(profile: string): string {
    switch (profile) {
      case 'Investigator': return 'Inv';
      case 'Realistic': return 'Real';
      case 'Enterprising': return 'Ent';
      case 'Artistic': return 'Art';
      case 'Social': return 'Soc';
      case 'Conventional': return 'Conv';
      default: return profile.substring(0, 3);
    }
  }

  getShortDate(date: string): string {
    // Convert DD/MM/YYYY to DD/MM
    return date.substring(0, 5);
  }
}
