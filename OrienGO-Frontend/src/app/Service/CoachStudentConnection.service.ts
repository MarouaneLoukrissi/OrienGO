import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CoachStudentConnectionResponseDTO } from '../model/dto/CoachStudentConnectionResponse.dto';
import { CoachStudentConnectionUpdateDTO } from '../model/dto/CoachStudentConnectionUpdate.dto';
import { CoachStudentConnectionCreateDTO } from '../model/dto/CoachStudentConnectionCreate.dto';
import { TestResultAverageDTO } from '../model/dto/TestResultAverage.dto';
import { TestResultProfilesDTO } from '../model/dto/TestResultProfiles.dto';

@Injectable({
  providedIn: 'root'
})
export class CoachStudentConnectionService {

  private baseUrl = 'http://localhost:8888/api/coach-student';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<CoachStudentConnectionResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachStudentConnectionResponseDTO[]>>(this.baseUrl);
  }

  getById(id: number): Observable<ApiResponse<CoachStudentConnectionResponseDTO>> {
    return this.http.get<ApiResponse<CoachStudentConnectionResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  create(request: CoachStudentConnectionCreateDTO): Observable<ApiResponse<CoachStudentConnectionResponseDTO>> {
    return this.http.post<ApiResponse<CoachStudentConnectionResponseDTO>>(this.baseUrl, request);
  }

  update(id: number, request: CoachStudentConnectionUpdateDTO): Observable<ApiResponse<CoachStudentConnectionResponseDTO>> {
    return this.http.put<ApiResponse<CoachStudentConnectionResponseDTO>>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  getByStudentIdAndStatusAndRequestedBy(studentId: number, status: string, requestedBy: string): Observable<ApiResponse<CoachStudentConnectionResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachStudentConnectionResponseDTO[]>>(
      `${this.baseUrl}/student/${studentId}/status/${status}/${requestedBy}`
    );
  }

  getByCoachIdAndStatusAndRequestedBy(coachId: number, status: string, requestedBy: string): Observable<ApiResponse<CoachStudentConnectionResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachStudentConnectionResponseDTO[]>>(
      `${this.baseUrl}/coach/${coachId}/status/${status}/${requestedBy}`
    );
  }

  // ✅ Count endpoints
  countByStudentIdAndStatusAndRequestedBy(studentId: number, status: string, requestedBy: string): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${this.baseUrl}/student/${studentId}/status/${status}/count`
    );
  }

  countByCoachIdAndStatusAndRequestedBy(coachId: number, status: string): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${this.baseUrl}/coach/${coachId}/status/${status}/count`
    );
  }

  // ✅ Dominant profile endpoint
  getCoacheesDominantProfile(coachId: number, status: string): Observable<ApiResponse<TestResultAverageDTO>> {
    return this.http.get<ApiResponse<TestResultAverageDTO>>(
      `${this.baseUrl}/coach/${coachId}/status/${status}/dominant-profile`
    );
  }

  // ✅ Average profiles endpoint
  getCoacheesAverageProfiles(coachId: number, status: string): Observable<ApiResponse<TestResultProfilesDTO>> {
    return this.http.get<ApiResponse<TestResultProfilesDTO>>(
      `${this.baseUrl}/coach/${coachId}/status/${status}/average-profiles`
    );
  }
}
