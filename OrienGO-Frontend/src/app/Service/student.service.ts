import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { StudentResponseDTO } from '../model/dto/StudentResponse.dto';
import { StudentCreateDTO } from '../model/dto/StudentCreate.dto';
import { StudentUpdateDTO } from '../model/dto/StudentUpdate.dto';
import { TestResultProfilesDTO } from '../model/dto/TestResultProfiles.dto';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrl = 'http://localhost:8888/api/student';

  constructor(private http: HttpClient) {}

  getCurrentUserId(): number {
    return 1;
  }

  // ===== GET methods =====
  getStudents(): Observable<ApiResponse<StudentResponseDTO[]>> {
    return this.http.get<ApiResponse<StudentResponseDTO[]>>(`${this.baseUrl}`);
  }

  getDeletedStudents(): Observable<ApiResponse<StudentResponseDTO[]>> {
    return this.http.get<ApiResponse<StudentResponseDTO[]>>(`${this.baseUrl}/deleted`);
  }

  getStudentById(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  getDeletedStudentById(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  getStudentByEmail(email: string): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/email/${email}`);
  }

  getDeletedStudentByEmail(email: string): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/deleted/email/${email}`);
  }

  // ===== RESTORE =====
  restoreStudent(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/restore/${id}`, {});
  }

  updateProfile(id: number, student: StudentUpdateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/profile/${id}`, student);
  }

  // ===== CREATE & UPDATE =====
  createStudent(student: StudentCreateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.post<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}`, student);
  }

  updateStudent(id: number, student: StudentCreateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/${id}`, student);
  }

  // ===== DELETE methods =====
  hardDeleteStudent(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  softDeleteStudent(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  getAverageProfiles(deleted: boolean = false): Observable<ApiResponse<TestResultProfilesDTO>> {
    return this.http.get<ApiResponse<TestResultProfilesDTO>>(
      `${this.baseUrl}/average-profiles?deleted=${deleted}`
    );
  }
}
