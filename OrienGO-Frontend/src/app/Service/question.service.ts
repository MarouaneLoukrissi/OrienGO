import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { QuestionResponseDTO } from '../model/dto/QuestionResponse.dto';
import { QuestionDTO } from '../model/dto/Question.dto';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private baseUrl = 'http://localhost:8888/api/questions';

  constructor(private http: HttpClient) {}

  getQuestions(): Observable<ApiResponse<QuestionResponseDTO[]>> {
    return this.http.get<ApiResponse<QuestionResponseDTO[]>>(`${this.baseUrl}`);
  }

  getDeletedQuestions(): Observable<ApiResponse<QuestionResponseDTO[]>> {
    return this.http.get<ApiResponse<QuestionResponseDTO[]>>(`${this.baseUrl}/deleted`);
  }

  getQuestionById(id: number): Observable<ApiResponse<QuestionResponseDTO>> {
    return this.http.get<ApiResponse<QuestionResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  getDeletedQuestionById(id: number): Observable<ApiResponse<QuestionResponseDTO>> {
    return this.http.get<ApiResponse<QuestionResponseDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  countQuestions(deleted: boolean = false): Observable<ApiResponse<number>> {
    let params = new HttpParams().set('deleted', deleted);
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/count`, { params });
  }

  createQuestion(question: QuestionDTO): Observable<ApiResponse<QuestionResponseDTO>> {
    return this.http.post<ApiResponse<QuestionResponseDTO>>(`${this.baseUrl}`, question);
  }

  updateQuestion(id: number, question: QuestionDTO): Observable<ApiResponse<QuestionResponseDTO>> {
    return this.http.put<ApiResponse<QuestionResponseDTO>>(`${this.baseUrl}/${id}`, question);
  }

  softDeleteQuestion(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  hardDeleteQuestion(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  restoreQuestion(id: number): Observable<ApiResponse<QuestionResponseDTO>> {
    return this.http.put<ApiResponse<QuestionResponseDTO>>(`${this.baseUrl}/restore/${id}`, {});
  }
}
