import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoachResponseDTO } from '../model/dto/CoachResponse.dto';
import { ApiResponse } from '../model/ApiResponse';
import { CoachCreateDTO } from '../model/dto/CoachCreate.dto';
import { CoachUpdateProfileDTO } from '../model/dto/CoachUpdateProfile.dto';

@Injectable({
  providedIn: 'root'
})
export class CoachService {

  private apiUrl = 'http://localhost:8888/api/coach'; // adjust backend port

  constructor(private http: HttpClient) {}

  // ---- Get all active coaches ----
  getCoaches(): Observable<ApiResponse<CoachResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachResponseDTO[]>>(this.apiUrl);
  }

  // ---- Get deleted coaches ----
  getDeletedCoaches(): Observable<ApiResponse<CoachResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachResponseDTO[]>>(`${this.apiUrl}/deleted`);
  }

  // ---- Get coach by ID ----
  getCoachById(id: number): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/${id}`);
  }

  // ---- Get deleted coach by ID ----
  getDeletedCoachById(id: number): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/deleted/${id}`);
  }

  // ---- Get coach by email ----
  getCoachByEmail(email: string): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/email/${email}`);
  }

  // ---- Get deleted coach by email ----
  getDeletedCoachByEmail(email: string): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/deleted/email/${email}`);
  }

  // ---- Create coach ----
  createCoach(coach: CoachCreateDTO): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.post<ApiResponse<CoachResponseDTO>>(this.apiUrl, coach);
  }

  // ---- Update coach ----
  updateCoach(id: number, coach: CoachCreateDTO): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.put<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/${id}`, coach);
  }

  // ---- Update coach ----
  updateProfileCoach(id: number, coach: CoachUpdateProfileDTO): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.put<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/profile/${id}`, coach);
  }

  // ---- Soft delete coach ----
  softDeleteCoach(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  // ---- Hard delete coach ----
  hardDeleteCoach(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/hard/${id}`);
  }

  // ---- Restore coach ----
  restoreCoach(id: number): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.put<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/restore/${id}`, {});
  }
}
