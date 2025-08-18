import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, NavigationStart, Route, Router } from '@angular/router';
import { TestQuestionService } from '../../../Service/testQuestion.service';
import { QuestionResponseDTO } from '../../../model/dto/QuestionResponseDTO';
import { TestService } from '../../../Service/test.service';
import { NotificationService } from '../../../Service/notification.service';
import { Question } from './data/questions';
import { TestSaveDTO } from '../../../model/dto/TestSaveDTO';
import { TestStatus } from '../../../model/enum/TestStatus';
import { TestResultService } from '../../../Service/testResult.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-express-test',
  templateUrl: './express-test.component.html',
  styleUrl: './express-test.component.css'
})
export class ExpressTestComponent implements OnInit, OnDestroy {

  private routerEventsSub?: Subscription;
  private isSubmitted = false;

  userId : number = 1;
  testId: string | null = null;  
  totalQuestions = 18;
  currentQuestionIndex = 0;
  selectedAnswer: number | null = null;
  answeredQuestions: number = 0;
  timeRemaining = 900; // 15 minutes in seconds
  timeElapsed: number | null = null;
  startDateTime: Date | null = null;
  timerLoading = true;
  timerIntervalId?: any;
  loading = false; // default off

  questions: Question[] = [];

  answers: Map<number, number> = new Map<number, number>();

  constructor(
    private notificationService: NotificationService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute,
    private testQuestionService: TestQuestionService,
    private testService: TestService,
    private testResultService: TestResultService
  ) {}

  ngOnInit() {
    this.answers = new Map<number, number>();

    this.route.paramMap.subscribe(params => {
      this.testId = params.get('testId');

      if (this.testId) {
        this.loadQuestions(this.testId);
        this.getTimeElapsed();  // timer starts here after startDateTime is set
      } else {
        this.notificationService.showError('No test ID found in URL.');
      }
    });

    this.routerEventsSub = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart && !this.isSubmitted) {
        this.saveQuestions(TestStatus.PENDING, null);
      }
    });
  }

  // 🔹 Detect page refresh / tab close / direct URL change
  @HostListener('window:beforeunload', ['$event'])
  unloadHandler(event: Event) {
    if (!this.isSubmitted) {
      this.saveQuestions(TestStatus.PENDING, null);
    }
  }

  private loadQuestions(testId: string) {
    this.testQuestionService.getTestQuestionsWithResponses(Number(testId)).subscribe({
      next: (res) => {
        this.questions = res.data.map(q => ({
          id: q.question.id,
          text: q.question.text,
          answerOptions: [...q.question.answerOptions].sort((a, b) => a.optionIndex - b.optionIndex),
          chosenAnswer: q.chosenAnswer ?? null
        }));
        this.totalQuestions = this.questions.length;

         // Fill the answers map from backend data
        this.questions.forEach(q => {
          if (q.chosenAnswer !== null) {
            this.answers.set(q.id, q.chosenAnswer.optionIndex - 1);
          }
        });
        // Find the first unanswered question
        const firstUnansweredIndex = this.questions.findIndex(q => !this.answers.has(q.id));
        this.currentQuestionIndex = firstUnansweredIndex !== -1 ? firstUnansweredIndex : 0;

        const saved = this.answers.get(this.questions[this.currentQuestionIndex].id);
        this.selectedAnswer = saved !== undefined ? saved : null;
        // Update answered count
        this.updateAnsweredCount();
        
      },
      error: (err) => {
        const serverMessage = err.error?.message || 'Unknown error occurred';
        this.notificationService.showError(serverMessage);        
        console.log(err);
      }
    });
  }

  get currentQuestion(): QuestionResponseDTO {
    return this.questions[this.currentQuestionIndex];
  }

  goBack() {
    this.location.back();
  }

  selectAnswer(optionIndex: number) {
    if (this.currentQuestion) {
      this.selectedAnswer = optionIndex;
      // Save by questionId instead of array index
      this.answers.set(this.currentQuestion.id, optionIndex);
      this.updateAnsweredCount();
    }
  }

  nextQuestion() {
    if (this.selectedAnswer !== null) {
      this.answers.set(this.currentQuestion.id, this.selectedAnswer);
      this.updateAnsweredCount();

      if (this.currentQuestionIndex < this.totalQuestions - 1) {
        this.saveQuestions(TestStatus.PENDING, null)
        this.currentQuestionIndex++;
        // Load saved selection if exists
        const saved = this.answers.get(this.currentQuestion.id);
        this.selectedAnswer = saved !== undefined ? saved : null;
      } else {
        this.submitTest();
      }
    }
  }
  
  previousQuestion() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
      const saved = this.answers.get(this.currentQuestion.id);
      this.selectedAnswer = saved !== undefined ? saved : null;
    }
  }

  updateAnsweredCount() {
    this.answeredQuestions = this.answers.size;
  }

  submitTest() {
    const now = new Date().toISOString(); // current local time in ISO format
    this.isSubmitted = true;
    this.saveQuestions(TestStatus.COMPLETED, now)
    
  }
  
  private saveQuestions(testStatus: TestStatus, completedAt: string | null) {
    if (!this.testId) {
      this.notificationService.showError('Invalid test ID');
      return;
    }

    if (this.answers.size === 0 && testStatus === TestStatus.PENDING) {
      this.testService.softDeleteTest(Number(this.testId)).subscribe({
        next: () => {
          this.notificationService.showSuccess('No answers provided, skipping save.');
        },
        error: (err) => {
          console.error('Failed to delete test', err);
        }
      });
      return;
    }
    // Convert Map to plain object
    const answersObj: { [key: number]: number } = {};
    this.answers.forEach((value, key) => {
      answersObj[key] = value + 1;
    });
    const saveTestDTO: TestSaveDTO = {
      testId: Number(this.testId),
      answers: answersObj,
      durationMinutes: this.timeElapsed??0,
      status: testStatus,
      completedAt: completedAt,
      answeredQuestionsCount: Number(this.answeredQuestions)
    };
    this.testService.saveUncompletedTest(saveTestDTO).subscribe({
      next: (res) => {
        if(testStatus === TestStatus.COMPLETED){
          // Handle test submission
          this.handleResultTestCalculation()
        }
        //console.log('Test saved', res);
      },
      error: (err) => {
        this.notificationService.showError(err.error?.message || 'Unknown error occurred');
        console.error(err);
        //console.error('Save failed', err);
      }
    });
  }

  handleResultTestCalculation(): void {
    this.loading = true;

    this.testResultService.createSavedTestResult(Number(this.testId))
      .subscribe({
        next: (res) => {
          this.loading = false;
          const newTestId = res.data.testId;
          this.notificationService.showSuccess(res.message);
          this.router.navigate([`student/test/results/${newTestId}`]);      
        },
        error: (err) => {
          this.loading = false; // stop spinner on error
          const serverMessage = err.error?.message || 'Unknown error occurred';
          this.notificationService.showError(serverMessage);
          console.log(err);
        }
      });
  }


  startTimer() {
    if (this.timeElapsed === null) return;

    this.timerIntervalId = setInterval(() => {
      this.timeElapsed!++; // increment seconds
    }, 1000);
  }
  ngOnDestroy() {
    if (!this.isSubmitted) {
      this.saveQuestions(TestStatus.PENDING, null);
    }
    if (this.timerIntervalId) clearInterval(this.timerIntervalId);
    if (this.routerEventsSub) this.routerEventsSub.unsubscribe();
  }

  /*startTimer() {
    const timer = setInterval(() => {
      this.timeRemaining--;
      if (this.timeRemaining <= 0) {
        clearInterval(timer);
        this.submitTest();
      }
    }, 1000);
  }*/
  
  formatElapsedTime(seconds: number): string {
    const years = Math.floor(seconds / (365 * 24 * 3600));
    seconds %= 365 * 24 * 3600;

    const months = Math.floor(seconds / (30 * 24 * 3600));
    seconds %= 30 * 24 * 3600;

    const days = Math.floor(seconds / (24 * 3600));
    seconds %= 24 * 3600;

    const hours = Math.floor(seconds / 3600);
    seconds %= 3600;

    const minutes = Math.floor(seconds / 60);
    seconds %= 60;

    const parts = [];
    if (years > 0) parts.push(`${years} year${years > 1 ? 's' : ''}`);
    if (months > 0) parts.push(`${months} month${months > 1 ? 's' : ''}`);
    if (days > 0) parts.push(`${days} day${days > 1 ? 's' : ''}`);
    if (hours > 0) parts.push(`${hours} hour${hours > 1 ? 's' : ''}`);
    if (minutes > 0) parts.push(`${minutes} minute${minutes > 1 ? 's' : ''}`);
    if (seconds > 0) parts.push(`${seconds} second${seconds > 1 ? 's' : ''}`);

    return parts.length > 0 ? parts.join(' ') : '0 seconds';
  }

  formatTime(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }

  private getTimeElapsed() {
    this.testService.getTestById(Number(this.testId)).subscribe({
      next: (res) => {
        const duration = Number(res.data?.durationMinutes); // actually in seconds
        if (duration !== null && duration !== undefined) {
          this.timeElapsed = duration; // already seconds
          console.log(this.timeElapsed)
        } else {
          this.timeElapsed = 0;
        }

        this.startTimer(); // start incrementing from that time
        this.timerLoading = false;
      },
      error: (err) => {
        this.timerLoading = false;
        this.notificationService.showError('Failed to load test elapsed time.');
      }
    });
  }

}
