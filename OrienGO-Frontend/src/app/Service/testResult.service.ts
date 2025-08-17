import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { TestResultResponseDTO } from '../model/dto/TestResultResponseDTO';
import { TestResultCreateDTO } from '../model/dto/TestResultCreateDTO';
import { TestResultMapMediaDTO } from '../model/dto/TestResultMapMediaDTO';

@Injectable({
  providedIn: 'root'
})
export class TestResultService {
  private baseUrl = 'http://localhost:8888/api/test-results';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<TestResultResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResultResponseDTO[]>>(this.baseUrl);
  }

  getById(id: number): Observable<ApiResponse<TestResultResponseDTO>> {
    return this.http.get<ApiResponse<TestResultResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  getByTestId(testId: number): Observable<ApiResponse<TestResultResponseDTO>> {
    return this.http.get<ApiResponse<TestResultResponseDTO>>(`${this.baseUrl}/test/${testId}`);
  }

  getByStudentId(studentId: number): Observable<ApiResponse<TestResultResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResultResponseDTO[]>>(`${this.baseUrl}/student/${studentId}`);
  }

  // This corresponds to POST /{testId}/unsaved-test/submit with a body
  createUnsavedTestResult(dto: TestResultCreateDTO): Observable<ApiResponse<TestResultResponseDTO>> {
    return this.http.post<ApiResponse<TestResultResponseDTO>>(`${this.baseUrl}/${dto.testId}/unsaved-test/submit`, dto);
  }

  // This corresponds to POST /{testId}/submit with just path param, no body
  createSavedTestResult(testId: number): Observable<ApiResponse<TestResultResponseDTO>> {
    return this.http.post<ApiResponse<TestResultResponseDTO>>(`${this.baseUrl}/${testId}/submit`, null);
  }

  mapTestResultToMedia(dto: TestResultMapMediaDTO): Observable<ApiResponse<TestResultResponseDTO>> {
    return this.http.post<ApiResponse<TestResultResponseDTO>>(
      `${this.baseUrl}/map-to-media`,
      dto
    );
  }
}