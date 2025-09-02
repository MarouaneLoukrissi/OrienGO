import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CoachResponseDTO } from '../model/dto/CoachResponse.dto';
import { CoachCreateDTO } from '../model/dto/CoachCreate.dto';
import { CoachUpdateProfileDTO } from '../model/dto/CoachUpdateProfile.dto';
import { CoachReturnDTO } from '../model/dto/CoachReturn.dto';
import { CoachDTO } from '../model/dto/Coach.dto';
import { CoachModifyDTO } from '../model/dto/CoachModify.dto';

@Injectable({
  providedIn: 'root'
})
export class CoachService {
  private apiUrl = 'http://localhost:8888/api/coach'; // adjust if needed

  constructor(private http: HttpClient) {}

  // ---- Get all coaches (Admin view) ----
  getCoaches(): Observable<ApiResponse<CoachReturnDTO[]>> {
    return this.http.get<ApiResponse<CoachReturnDTO[]>>(`${this.apiUrl}`);
  }

  // ---- Get active coaches (User view) ----
  getActiveCoaches(): Observable<ApiResponse<CoachResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachResponseDTO[]>>(`${this.apiUrl}/active`);
  }

  // ---- Get deleted coaches (User view) ----
  getDeletedCoaches(): Observable<ApiResponse<CoachResponseDTO[]>> {
    return this.http.get<ApiResponse<CoachResponseDTO[]>>(`${this.apiUrl}/deleted`);
  }

  // ---- Get deleted coaches (Admin view) ----
  getDeletedCoachesForAdmin(): Observable<ApiResponse<CoachReturnDTO[]>> {
    return this.http.get<ApiResponse<CoachReturnDTO[]>>(`${this.apiUrl}/admin/deleted`);
  }

  // ---- Get coach by ID (User view) ----
  getCoachById(id: number): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/${id}`);
  }

  // ---- Get coach by ID (Admin view) ----
  getCoachByIdForAdmin(id: number): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.get<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin/${id}`);
  }

  // ---- Get deleted coach by ID (User view) ----
  getDeletedCoachById(id: number): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/deleted/${id}`);
  }

  // ---- Get deleted coach by ID (Admin view) ----
  getDeletedCoachByIdForAdmin(id: number): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.get<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin/deleted/${id}`);
  }

  // ---- Get coach by email (User view) ----
  getCoachByEmail(email: string): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/email/${email}`);
  }

  // ---- Get coach by email (Admin view) ----
  getCoachByEmailForAdmin(email: string): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.get<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin/email/${email}`);
  }

  // ---- Get deleted coach by email (User view) ----
  getDeletedCoachByEmail(email: string): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.get<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/deleted/email/${email}`);
  }

  // ---- Get deleted coach by email (Admin view) ----
  getDeletedCoachByEmailForAdmin(email: string): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.get<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin/deleted/email/${email}`);
  }

  // ---- Create coach (User view) ----
  createCoach(coach: CoachCreateDTO): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.post<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}`, coach);
  }

  // ---- Create coach (Admin view) ----
  createCoachForAdmin(coach: CoachDTO): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.post<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin`, coach);
  }

  // ---- Update coach (User view) ----
  updateCoach(id: number, coach: CoachCreateDTO): Observable<ApiResponse<CoachResponseDTO>> {
    return this.http.put<ApiResponse<CoachResponseDTO>>(`${this.apiUrl}/${id}`, coach);
  }

  // ---- Update coach (Admin view) ----
  updateCoachForAdmin(id: number, coach: CoachModifyDTO): Observable<ApiResponse<CoachReturnDTO>> {
    return this.http.put<ApiResponse<CoachReturnDTO>>(`${this.apiUrl}/admin/${id}`, coach);
  }

  // ---- Update coach profile ----
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
