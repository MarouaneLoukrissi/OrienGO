import { Component } from '@angular/core';
import { Question, QUESTIONS } from './data/questions';
import { ANSWER_OPTIONS, AnswerOption } from './data/answerOptions';

@Component({
  selector: 'app-full-test',
  templateUrl: './full-test.component.html',
  styleUrl: './full-test.component.css'
})
export class FullTestComponent {
  totalQuestions = 60;
  currentQuestionIndex = 0;
  selectedAnswer: number | null = null;
  answeredQuestions = 0;
  timeRemaining = 3600;
  
  questions: Question[] = QUESTIONS;

  answerOptions : AnswerOption[] = ANSWER_OPTIONS;

  answers: (number | null)[] = new Array(this.totalQuestions).fill(null);

  ngOnInit() {
    this.answers = new Array(this.totalQuestions).fill(null);
    this.startTimer();
  }

  get currentQuestion(): Question {
    return this.questions[this.currentQuestionIndex];
  }

  selectAnswer(optionIndex: number) {
    this.selectedAnswer = optionIndex;
  }

  nextQuestion() {
    if (this.selectedAnswer !== null) {
      this.answers[this.currentQuestionIndex] = this.selectedAnswer;
      this.updateAnsweredCount();
      
      if (this.currentQuestionIndex < this.totalQuestions - 1) {
        this.currentQuestionIndex++;
        this.selectedAnswer = this.answers[this.currentQuestionIndex];
      } else {
        this.submitTest();
      }
    }
  }

  previousQuestion() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
      this.selectedAnswer = this.answers[this.currentQuestionIndex];
    }
  }

  updateAnsweredCount() {
    this.answeredQuestions = this.answers.filter(answer => answer !== null).length;
  }

  submitTest() {
    // Handle test submission
    console.log('Test submitted:', this.answers);
    alert('Full test completed!');
  }

  startTimer() {
    const timer = setInterval(() => {
      this.timeRemaining--;
      if (this.timeRemaining <= 0) {
        clearInterval(timer);
        this.submitTest();
      }
    }, 1000);
  }

  formatTime(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }
}
