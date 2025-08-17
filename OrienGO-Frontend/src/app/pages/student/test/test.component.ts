import { Component, EventEmitter, OnInit, Output, OnDestroy } from '@angular/core';
import { TestCreateDTO } from '../../../model/dto/TestCreateDTO';
import { TestType } from '../../../model/enum/TestType';
import { Router, NavigationEnd } from '@angular/router';
import { TestService } from '../../../Service/test.service';
import { TestQuestionService } from '../../../Service/testQuestion.service';
import { NotificationService } from '../../../Service/notification.service';
import { TestStatus } from '../../../model/enum/TestStatus';
import { delay, filter, Subscription } from 'rxjs';

interface TestResult {
  type: TestType,
  date: string;
  duration: number;
  progress: number,
}

interface ChartData {
  date: string;
  profile: string;
  score: number;
  color: string;
}

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrl: './test.component.css'
})
export class TestComponent implements OnInit, OnDestroy {

  @Output() testStarted = new EventEmitter<number>();

  private routerSubscription: Subscription = new Subscription();

  getTextType(type: TestType){
    return (type === TestType.FAST) ? 'Express Test' : 'Full Test'
  }

  userId : number = 1;
  testId: string | null = null;

  fastTestloading = false;
  fullTestloading = false;

  currentPage: number = 1;
  itemsPerPage: number = 3;

  tests: any[] = [];

  getUncompletedTests() {
    return this.tests.filter(test => test.progress < 100);
  }

  getFilteredUncompletedTests() {
    const uncompletedTests = this.getUncompletedTests();
    const activeFilter = this.filters.find(f => f.active);
    
    if (!activeFilter || activeFilter.id === 'all') {
      return uncompletedTests;
    }

    return uncompletedTests.filter(test => {
      switch (activeFilter.id) {
        case '3months':
          const threeMonthsAgo = new Date();
          threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
          const testDate = test.rawDate || new Date(test.date);
          return testDate >= threeMonthsAgo;
        
        case 'full':
          return test.type === TestType.COMPLETE;
        
        case 'express':
          return test.type === TestType.FAST;
        
        default:
          return true;
      }
    });
  }

  getPaginatedUncompletedTests() {
    const filteredTests = this.getFilteredUncompletedTests();
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return filteredTests.slice(startIndex, endIndex);
  }

  getTotalPages(): number {
    const filteredTests = this.getFilteredUncompletedTests();
    return Math.ceil(filteredTests.length / this.itemsPerPage);
  }

  getPageNumbers(): number[] {
    const totalPages = this.getTotalPages();
    const pages: number[] = [];
    
    for (let i = 1; i <= totalPages; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.getTotalPages()) {
      this.currentPage = page;
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.getTotalPages()) {
      this.currentPage++;
    }
  }

  deleteTest(testId: number): void {
    if (confirm('Are you sure you want to delete this test?')) {
      this.tests = this.tests.filter(test => test.id !== testId);
      
      this.testService.softDeleteTest(Number(testId))
      .subscribe({
        next: (res) => {
          this.notificationService.showSuccess(res.message);
          this.refreshTestData(); // Refresh data after deletion
        },
        error: (err) => {
          const serverMessage =
            err.error?.message || 'Unknown error occurred while deleting test.';
          this.notificationService.showError(serverMessage);
          console.error(err);
          this.refreshTestData(); // Refresh data even on error to get correct state
        }
      });

      // Adjust current page if necessary
      const totalPages = this.getTotalPages();
      if (this.currentPage > totalPages && totalPages > 0) {
        this.currentPage = totalPages;
      } else if (totalPages === 0) {
        this.currentPage = 1;
      }
    }
  }

  resumeTest(testId: number): void {
    const test = this.tests.find(t => t.id === testId);
    if (test) {
      this.router.navigate([
        '/student', 
        'test', 
        (test.type == TestType.FAST) ? 'express' : 'full', 
        test.id
      ]);      
    }
  }

  filters = [
    { id: 'all', label: 'All tests', active: true },
    { id: '3months', label: 'Last 3 months', active: false },
    { id: 'full', label: 'Full tests', active: false },
    { id: 'express', label: 'Express tests', active: false }
  ];

  chartData: ChartData[] = [];

  allTestsChartData: ChartData[] = [];

  last3MonthsChartData: ChartData[] = [];

  fullTestsChartData: ChartData[] = [];

  expressTestsChartData: ChartData[] = [];

  allTestResults: TestResult[] = [];

  ngOnInit(): void {
    if (this.userId) {
      this.loadPendingTests(this.userId);
    } else {
      this.notificationService.showError('No test ID found in URL.');
    }

    // Listen to router events to refresh data when navigating back
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        // Check if we're navigating to this component's route
        if (event.url.includes('/student/test') && !event.url.includes('/express') && !event.url.includes('/full')) {
          this.refreshTestData();
        }
      });
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  // New method to refresh test data
  refreshTestData(): void {
    if (this.userId) {
      this.loadPendingTests(this.userId);
    }
  }

  // Add a manual refresh button method if needed
  onRefreshClick(): void {
    this.refreshTestData();
  }

  private formatDuration(totalSeconds: number): string {
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

  private loadPendingTests(userId: number) {
    this.testService.getTestsByStudentIdAndStatus(Number(userId), TestStatus.PENDING)
      .subscribe({
        next: (res) => {
          if (res && Array.isArray(res.data)) {
            this.tests = res.data
              .map((test: any) => ({
                id: test.id,
                type: test.type,
                rawDate: test.startedAt ? new Date(test.startedAt) : null,
                date: test.startedAt
                  ? new Date(test.startedAt).toISOString().split('T')[0]
                  : '',
                duration: this.formatDuration(test.durationMinutes),
                progress: this.calculateProgress(test),

                // Hidden property for sorting
                _updatedAt: test.updatedAt ? new Date(test.updatedAt) : null
              }))
              .filter(test => test.progress > 0 && test.progress < 100)
              .sort((a, b) =>
                (b._updatedAt?.getTime() ?? 0) - (a._updatedAt?.getTime() ?? 0)
              );
          } else {
            this.tests = [];
          }
        },
        error: (err) => {
          const serverMessage =
            err.error?.message || 'Unknown error occurred while loading tests.';
          this.notificationService.showError(serverMessage);
          console.error(err);
          this.tests = [];
        }
      });
  }

  private calculateProgress(test: any): number {
    if (!test.questionsCount || !test.durationMinutes) {
      return 0;
    }

    const progress = (test.answeredQuestionsCount / test.questionsCount) * 100;

    // Only allow 1% to 99% for in-progress tests
    if (progress <= 0) return 0;
    if (progress >= 100) return 100;

    return Math.floor(progress);
  }

  setActiveFilter(filterId: string): void {
    this.filters.forEach(filter => {
      filter.active = filter.id === filterId;
    });
    this.updateChartData();
    // Reset to first page when filter changes
    this.currentPage = 1;
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

  getFilteredTests(): TestResult[] {
    const activeFilter = this.filters.find(f => f.active);
    if (!activeFilter) return this.allTestResults;

    switch (activeFilter.id) {
      case 'all':
        return this.allTestResults;
      case '3months':
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
    const [day, month, year] = dateString.split('/').map(Number);
    return new Date(year, month - 1, day);
  }

  dto : TestCreateDTO = {
    studentId: this.userId,
    type: TestType.FAST,
  };

  startTest(questionCount: number) {
    this.testStarted.emit(questionCount);
  }

  constructor(
    private testService: TestService,
    private testQuestionService: TestQuestionService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  handleFastTestCreation(event: Event): void {
    this.fastTestloading = true;
    this.dto.type = TestType.FAST;

    this.testService.createTest(this.dto).subscribe({
      next: (res) => {
        this.fastTestloading = false;
        const newTestId = res.data.id;
        this.notificationService.showSuccess(res.message);
        this.router.navigate(['/student/test/express', newTestId]);
      },
      error: (err) => {
        this.fastTestloading = false;
        const serverMessage = err.error?.message || 'Unknown error occurred';
        this.notificationService.showError(serverMessage);
        console.log(err)
      }
    });
  }

  handleFullTestCreation(event: Event): void {
    this.fullTestloading = true;
    this.dto.type = TestType.COMPLETE;

    this.testService.createTest(this.dto).subscribe({
      next: (res) => {
        this.fullTestloading = false;
        const newTestId = res.data.id;
        this.notificationService.showSuccess(res.message);
        this.router.navigate(['/student/test/full', newTestId]);
      },
      error: (err) => {
        this.fullTestloading = false;
        const serverMessage = err.error?.message || 'Unknown error occurred';
        this.notificationService.showError(serverMessage);
        console.log(err)
      }
    });
  }
}