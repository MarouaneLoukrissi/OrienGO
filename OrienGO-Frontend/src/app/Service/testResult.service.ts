import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { TestResultResponseDTO } from '../model/dto/TestResultResponse.dto';
import { TestResultCreateDTO } from '../model/dto/TestResultCreate.dto';
import { TestResultMapMediaDTO } from '../model/dto/TestResultMapMedia.dto';
import { TestResultAverageDTO } from '../model/dto/TestResultAverage.dto';
import { TestResultProfilesDTO } from '../model/dto/TestResultProfiles.dto';

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

  getDominantProfileByStudentId(studentId: number): Observable<ApiResponse<TestResultAverageDTO>> {
    return this.http.get<ApiResponse<TestResultAverageDTO>>(`${this.baseUrl}/student/average/${studentId}`);
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

  getProfilesByStudentId(studentId: number): Observable<ApiResponse<TestResultProfilesDTO>> {
    return this.http.get<ApiResponse<TestResultProfilesDTO>>(
      `${this.baseUrl}/student/profiles/${studentId}`
    );
  }
}