import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { TestQuestionResponseDTO } from '../model/dto/TestQuestionResponseDTO';


@Injectable({
  providedIn: 'root'
})
export class TestQuestionService {

  private baseUrl = 'http://localhost:8888/api/test-questions';

  // Store questions in memory for reuse
  private questionsSubject = new BehaviorSubject<TestQuestionResponseDTO[]>([]);
  public questions$ = this.questionsSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Method to get test questions with responses for a given testId
  getTestQuestionsWithResponses(testId: number): Observable<ApiResponse<TestQuestionResponseDTO[]>> {
    return this.http
      .get<ApiResponse<TestQuestionResponseDTO[]>>(`${this.baseUrl}/${testId}/with-responses`)
      .pipe(
        tap(response => {
          if (response.data) {
            this.questionsSubject.next(response.data);
          }
        })
      );
  }
  
  // Optional: Get current value synchronously
  getCurrentQuestions(): TestQuestionResponseDTO[] {
    return this.questionsSubject.value;
  }
}
