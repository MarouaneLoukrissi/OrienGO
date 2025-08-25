import { Component, OnInit } from '@angular/core';
import { Observable, forkJoin, map, switchMap, of } from 'rxjs';
import { TestService } from '../../../Service/test.service';
import { TestResultService } from '../../../Service/testResult.service';
import { MediaService } from '../../../Service/media.service';
import { TestResponseDTO } from '../../../model/dto/TestResponse.dto';
import { ApiResponse } from '../../../model/ApiResponse';
import { Category } from '../../../model/enum/Category.enum';
import { TestType } from '../../../model/enum/TestType.enum';
import { TestResultResponseDTO } from '../../../model/dto/TestResultResponse.dto';
import { TestStatus } from '../../../model/enum/TestStatus.enum';
import { CommonModule } from '@angular/common';

interface TestResult {
  id: string;
  type: string;
  date: string;
  rawDate: string;
  duration: string;
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
  // Additional details for popup
  fullTestData?: TestResponseDTO;
  fullResultData?: TestResultResponseDTO;
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
export class HistoryComponent implements OnInit {

  userId: number = 1;

  // Loading states
  isLoading = true;
  hasError = false;
  errorMessage = '';
  isDownloadingPdf = false;

  // Pagination properties
  currentPage = 1;
  itemsPerPage = 4;
  totalPages = 0;
  totalItems = 0;

  // Details popup properties
  showDetailsPopup = false;
  selectedTest: TestResult | null = null;

  // Data properties
  statsCards: StatCard[] = [];
  allTestResults: TestResult[] = [];
  chartData: ChartData[] = [];

  // Filter data - separate for each filter type
  allTestsChartData: ChartData[] = [];
  last3MonthsChartData: ChartData[] = [];
  fullTestsChartData: ChartData[] = [];
  expressTestsChartData: ChartData[] = [];

  filters = [
    { id: 'all', label: 'All tests', active: true },
    { id: '3months', label: 'Last 3 months', active: false },
    { id: 'full', label: 'Full tests', active: false },
    { id: 'express', label: 'Express tests', active: false }
  ];

  // Math utilities for template
  Math = Math;

  getProfileBgColor(profile: string): string {
    const colors: { [key: string]: string } = {
      'Realistic': '#3b82f6',
      'Investigator': '#8b5cf6',
      'Artistic': '#ec4899',
      'Social': '#10b981',
      'Enterprising': '#ef4444',
      'Conventional': '#6366f1'
    };
    const hex = colors[profile] || '#999999';

    // Convert hex to rgba with 0.5 alpha
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return `rgba(${r}, ${g}, ${b}, 0.8)`;
  }

  profileColors = [
    { name: 'Realistic (R)', short: 'R', color: '#3b82f6' },
    { name: 'Investigator (I)', short: 'I', color: '#8b5cf6' },
    { name: 'Artistic (A)', short: 'A', color: '#ec4899' },
    { name: 'Social (S)', short: 'S', color: '#10b981' },
    { name: 'Enterprising (E)', short: 'E', color: '#ef4444' },
    { name: 'Conventional (C)', short: 'C', color: '#6366f1' }
  ];

  constructor(
    private testService: TestService,
    private testResultService: TestResultService,
    private mediaService: MediaService
  ) {}

  ngOnInit(): void {
    this.loadTestHistory();
  }

  // PDF Download functionality
  downloadPdf(testResult: TestResult): void {
    if (!testResult.fullResultData?.pdfId) {
      console.error('No PDF ID found for this test result');
      return;
    }

    this.isDownloadingPdf = true;
    
    this.mediaService.getMediaFileById(testResult.fullResultData.pdfId).subscribe({
      next: (blob: Blob) => {
        this.downloadBlob(blob, `RIASEC_Results_User_${this.userId}_Test_${testResult.fullResultData?.testId}_${new Date().toISOString().split('T')[0]}.pdf`);
        this.isDownloadingPdf = false;
      },
      error: (error) => {
        console.error('Error downloading PDF:', error);
        this.isDownloadingPdf = false;
        // You can add user notification here
        alert('Failed to download PDF. Please try again later.');
      }
    });
  }

  private downloadBlob(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  // Check if PDF is available for download
  isPdfAvailable(testResult: TestResult): boolean {
    return !!(testResult.fullResultData?.pdfId);
  }

  loadTestHistory(): void {
    this.isLoading = true;
    this.hasError = false;

    this.testService.getTestsByStudentIdAndStatus(this.userId, TestStatus.COMPLETED).pipe(
      switchMap((response: ApiResponse<TestResponseDTO[]>) => {
        const tests = response.data || [];
        
        if (tests.length === 0) {
          return of([]);
        }

        // Get test results for all completed tests
        const completedTests = tests.filter(test => test.status === 'COMPLETED');
        
        if (completedTests.length === 0) {
          return of([]);
        }

        const testResultRequests = completedTests.map(test =>
          this.testResultService.getByTestId(test.id).pipe(
            map((resultResponse: ApiResponse<TestResultResponseDTO>) => 
              this.transformToTestResult(test, resultResponse.data)
            )
          )
        );

        return forkJoin(testResultRequests);
      })
    ).subscribe({
      next: (testResults) => {
        this.allTestResults = this.processTestResults(testResults);
        this.calculatePagination();
        this.generateStatsCards();
        this.setupChartData();
        this.updateChartData();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading test history:', error);
        this.hasError = true;
        this.errorMessage = 'Failed to load test history. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  // Pagination methods
  calculatePagination(): void {
    const filteredTests = this.getFilteredTestsAll();
    this.totalItems = filteredTests.length;
    this.totalPages = Math.ceil(this.totalItems / this.itemsPerPage);
    
    // Reset to first page if current page is beyond total pages
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = 1;
    }
  }

  getPaginatedTests(): TestResult[] {
    const filteredTests = this.getFilteredTestsAll();
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return filteredTests.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    const halfRange = Math.floor(maxPagesToShow / 2);
    
    let startPage = Math.max(1, this.currentPage - halfRange);
    let endPage = Math.min(this.totalPages, startPage + maxPagesToShow - 1);
    
    if (endPage - startPage < maxPagesToShow - 1) {
      startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // Details popup methods
  openDetailsPopup(test: TestResult): void {
    this.selectedTest = test;
    this.showDetailsPopup = true;
    // Prevent body scrolling when modal is open
    document.body.style.overflow = 'hidden';
  }

  closeDetailsPopup(): void {
    this.showDetailsPopup = false;
    this.selectedTest = null;
    // Restore body scrolling
    document.body.style.overflow = 'auto';
  }

  formatNumber(value: number): string {
    return Number.isInteger(value) ? value.toString() : value.toFixed(1);
  }

  // Helper method to get score safely with proper typing
  getTestScore(test: TestResult, profileShort: string): number {
    const key = profileShort as keyof TestResult['scores'];
    return test.scores[key] || 0;
  }

  private transformToTestResult(test: TestResponseDTO, result: TestResultResponseDTO): TestResult {
    // Convert backend data to frontend format
    const scores = {
      R: Number(result.scores[Category.REALISTIC] || 0),
      I: Number(result.scores[Category.INVESTIGATIVE] || 0),
      A: Number(result.scores[Category.ARTISTIC] || 0),
      S: Number(result.scores[Category.SOCIAL] || 0),
      E: Number(result.scores[Category.ENTERPRISING] || 0),
      C: Number(result.scores[Category.CONVENTIONAL] || 0)
    };

    return {
      id: test.id.toString(),
      type: this.getTestTypeDisplay(test.type),
      date: test.startedAt,
      rawDate: test.completedAt,
      duration: this.formatDuration(test.durationMinutes),
      dominantProfile: this.getCategoryDisplay(result.dominantType),
      dominantScore: Number(result.scores[result.dominantType] || 0),
      scores: scores,
      isCurrent: false,
      fullTestData: test,
      fullResultData: result
    };
  }

  formatDuration(totalSeconds: number): string {
    if (!totalSeconds || totalSeconds <= 0) return '0 seconds';

    const years = Math.floor(totalSeconds / (365 * 24 * 3600));
    totalSeconds %= 365 * 24 * 3600;

    const months = Math.floor(totalSeconds / (30 * 24 * 3600));
    totalSeconds %= 30 * 24 * 3600;

    const days = Math.floor(totalSeconds / (24 * 3600));
    totalSeconds %= 24 * 3600;

    const hours = Math.floor(totalSeconds / 3600);
    totalSeconds %= 3600;

    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;

    const parts = [];
    if (years) parts.push(`${years} year${years > 1 ? 's' : ''}`);
    if (months) parts.push(`${months} month${months > 1 ? 's' : ''}`);
    if (days) parts.push(`${days} day${days > 1 ? 's' : ''}`);
    if (hours) parts.push(`${hours} hour${hours > 1 ? 's' : ''}`);
    if (minutes) parts.push(`${minutes} minute${minutes > 1 ? 's' : ''}`);
    if (seconds) parts.push(`${seconds} second${seconds > 1 ? 's' : ''}`);

    return parts.join(' ');
  }

  private getTestTypeDisplay(type: TestType): string {
    switch (type) {
      case TestType.COMPLETE:
        return 'RIASEC Full';
      case TestType.FAST:
        return 'RIASEC Express';
      default:
        return 'RIASEC Test';
    }
  }

  private getCategoryDisplay(category: Category): string {
    switch (category) {
      case Category.REALISTIC:
        return 'Realistic';
      case Category.INVESTIGATIVE:
        return 'Investigator';
      case Category.ARTISTIC:
        return 'Artistic';
      case Category.SOCIAL:
        return 'Social';
      case Category.ENTERPRISING:
        return 'Enterprising';
      case Category.CONVENTIONAL:
        return 'Conventional';
      default:
        return 'Unknown';
    }
  }

  formatDate(isoDate: string): string {
    const date = new Date(isoDate);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  private processTestResults(testResults: TestResult[]): TestResult[] {
    if (testResults.length === 0) return testResults;

    const sorted = testResults.sort((a, b) =>
      new Date(a.rawDate).getTime() - new Date(b.rawDate).getTime()
    );

    // Mark the most recent as current
    if (sorted.length > 0) {
      sorted[sorted.length-1].isCurrent = true;
    }

    return sorted;
  }

  private generateStatsCards(): void {
    if (this.allTestResults.length === 0) {
      this.statsCards = [
        { title: 'Tests passed', value: '0', subtitle: 'Tests passed', color: 'text-orange-500' },
        { title: 'Current profile', value: 'N/A', subtitle: 'Current profile', color: 'text-orange-500' },
        { title: 'Current score', value: '0%', subtitle: 'Current score', color: 'text-orange-500' },
        { title: 'Progress', value: '0%', subtitle: 'Progress', color: 'text-gray-500' }
      ];
      return;
    }

    // Sort by date to get the most recent
    const sortedResults = [...this.allTestResults].sort((a, b) =>
      new Date(b.rawDate).getTime() - new Date(a.rawDate).getTime()
    );

    const currentTest = sortedResults[0];
    const previousTest = sortedResults[1];

    // Calculate progress
    let progressValue = '0%';
    let progressColor = 'text-gray-500';

    if (previousTest && currentTest) {
      const progress = currentTest.dominantScore - previousTest.dominantScore;
      if (progress > 0) {
        progressValue = `+${this.formatNumber(progress)}%`;
        progressColor = 'text-green-500';
      } else if (progress < 0) {
        progressValue = `${this.formatNumber(progress)}%`;
        progressColor = 'text-red-500';
      } else {
        progressValue = '0%';
        progressColor = 'text-gray-500';
      }
    }

    this.statsCards = [
      { 
        title: 'Tests passed', 
        value: this.allTestResults.length.toString(), 
        subtitle: 'Tests passed', 
        color: 'text-orange-500' 
      },
      { 
        title: 'Current profile', 
        value: currentTest.dominantProfile, 
        subtitle: 'Current profile', 
        color: 'text-orange-500' 
      },
      { 
        title: 'Current score', 
        value: `${this.formatNumber(currentTest.dominantScore)}%`, 
        subtitle: 'Current score', 
        color: 'text-orange-500' 
      },
      { 
        title: 'Progress', 
        value: progressValue, 
        subtitle: 'Progress', 
        color: progressColor 
      }
    ];
  }

  private setupChartData(): void {
    this.allTestsChartData = this.generateChartData(this.allTestResults);

    // Generate filtered chart data
    this.last3MonthsChartData = this.generateChartData(
      this.getTestsFromLastMonths(3)
    );

    this.fullTestsChartData = this.generateChartData(
      this.allTestResults.filter(test => test.type.includes('Full'))
    );

    this.expressTestsChartData = this.generateChartData(
      this.allTestResults.filter(test => test.type.includes('Express'))
    );
  }

  private generateChartData(testResults: TestResult[]): ChartData[] {
    const profileColors = {
      'Realistic': '#3b82f6',
      'Investigator': '#8b5cf6',
      'Artistic': '#ec4899',
      'Social': '#10b981',
      'Enterprising': '#ef4444',
      'Conventional': '#6366f1'
    };

    return testResults
      .sort((a, b) => new Date(b.rawDate).getTime() - new Date(a.rawDate).getTime())
      .map(test => ({
        date: test.rawDate,
        profile: this.getProfileShort(test.dominantProfile),
        score: Number(test.dominantScore),
        color: profileColors[test.dominantProfile as keyof typeof profileColors] || '#6b7280'
      }));
  }

  getProfileShort(profile: string): string {
    switch (profile) {
      case 'Realistic':
        return 'R';
      case 'Investigator':
        return 'I';
      case 'Artistic':
        return 'A';
      case 'Social':
        return 'S';
      case 'Enterprising':
        return 'E';
      case 'Conventional':
        return 'C';
      default:
        return profile.charAt(0);
    }
  }

  private getTestsFromLastMonths(months: number): TestResult[] {
    const cutoffDate = new Date();
    cutoffDate.setMonth(cutoffDate.getMonth() - months);

    return this.allTestResults.filter(test => new Date(test.rawDate) >= cutoffDate);
  }

  getRiasecKeys(): (keyof TestResult['scores'])[] {
    return ['R', 'I', 'A', 'S', 'E', 'C'];
  }

  setActiveFilter(filterId: string): void {
    this.filters.forEach(filter => {
      filter.active = filter.id === filterId;
    });
    this.currentPage = 1; // Reset to first page when changing filter
    this.calculatePagination();
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

  // This method returns all filtered tests (used internally)
  getFilteredTestsAll(): TestResult[] {
    const activeFilter = this.filters.find(f => f.active);
    if (!activeFilter) return this.allTestResults;

    switch (activeFilter.id) {
      case 'all':
        return this.allTestResults;
      case '3months':
        return this.getTestsFromLastMonths(3);
      case 'full':
        return this.allTestResults.filter(test => test.type.includes('Full'));
      case 'express':
        return this.allTestResults.filter(test => test.type.includes('Express'));
      default:
        return this.allTestResults;
    }
  }

  // This method is used by template and returns paginated tests
  getFilteredTests(): TestResult[] {
    return this.getPaginatedTests();
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

  // Refresh data manually
  refreshData(): void {
    this.currentPage = 1;
    this.loadTestHistory();
  }
}