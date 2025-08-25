import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { UserService } from '../../../Service/user.service';
import { TestService } from '../../../Service/test.service';
import { QuestionService } from '../../../Service/question.service';
import { JobService } from '../../../Service/job.service';
import { StudentService } from '../../../Service/student.service';
import { ProfileScoreDTO } from '../../../model/dto/TestResultProfiles.dto';
import { Category } from '../../../model/enum/Category.enum';
import { TestType } from '../../../model/enum/TestType.enum';

interface DashboardStats {
  users: number;
  fullTests: number;
  questionnaires: number;
  profiles: number;
  expressTests: number;
}

interface ChartData {
  realistic: number;
  investigative: number;
  artistic: number;
  social: number;
  enterprising: number;
  conventional: number;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats = {
    users: 0,
    fullTests: 0,
    questionnaires: 0,
    profiles: 0,
    expressTests: 0
  };

  chartData: ChartData = {
    realistic: 0,
    investigative: 0,
    artistic: 0,
    social: 0,
    enterprising: 0,
    conventional: 0
  };

  isLoading = true;
  hasError = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private testService: TestService,
    private questionService: QuestionService,
    private jobService: JobService,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.isLoading = true;
    this.hasError = false;

    // Fetch all data simultaneously using forkJoin
    forkJoin({
      users: this.userService.countUsersByRoles(false, ['COACH', 'STUDENT']),
      fullTests: this.testService.getTestCountByType(TestType.COMPLETE, false),
      expressTests: this.testService.getTestCountByType(TestType.FAST, false),
      questionnaires: this.questionService.countQuestions(false),
      profiles: this.jobService.countJobs(),
      averageProfiles: this.studentService.getAverageProfiles(false)
    }).subscribe({
      next: (results) => {
        // Update stats
        this.stats = {
          users: results.users.data || 0,
          fullTests: results.fullTests.data || 0,
          expressTests: results.expressTests.data || 0,
          questionnaires: results.questionnaires.data || 0,
          profiles: results.profiles.data || 0
        };

        // Update chart data
        this.updateChartData(results.averageProfiles.data?.profiles || []);
        
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading dashboard data:', error);
        this.hasError = true;
        this.errorMessage = 'Failed to load dashboard data. Please try again.';
        this.isLoading = false;
      }
    });
  }

  private updateChartData(profiles: ProfileScoreDTO[]): void {
    // Initialize all categories to 0
    this.chartData = {
      realistic: 0,
      investigative: 0,
      artistic: 0,
      social: 0,
      enterprising: 0,
      conventional: 0
    };

    // Update with actual data
    profiles.forEach(profile => {
      switch (profile.category) {
        case Category.REALISTIC:
          this.chartData.realistic = Math.round(profile.percentage * 10) / 10;
          break;
        case Category.INVESTIGATIVE:
          this.chartData.investigative = Math.round(profile.percentage * 10) / 10;
          break;
        case Category.ARTISTIC:
          this.chartData.artistic = Math.round(profile.percentage * 10) / 10;
          break;
        case Category.SOCIAL:
          this.chartData.social = Math.round(profile.percentage * 10) / 10;
          break;
        case Category.ENTERPRISING:
          this.chartData.enterprising = Math.round(profile.percentage * 10) / 10;
          break;
        case Category.CONVENTIONAL:
          this.chartData.conventional = Math.round(profile.percentage * 10) / 10;
          break;
      }
    });
  }

  refreshData(): void {
    this.loadDashboardData();
  }
}