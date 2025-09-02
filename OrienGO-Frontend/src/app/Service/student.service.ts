import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { StudentResponseDTO } from '../model/dto/StudentResponse.dto';
import { StudentCreateDTO } from '../model/dto/StudentCreate.dto';
import { StudentUpdateDTO } from '../model/dto/StudentUpdate.dto';
import { TestResultProfilesDTO } from '../model/dto/TestResultProfiles.dto';
import { StudentReturnDTO } from '../model/dto/StudentReturn.dto';
import { StudentDTO } from './student.dto';
import { StudentModifyDTO } from '../model/dto/studentModify.dto';

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

  getStudentsForAdmin(): Observable<ApiResponse<StudentReturnDTO[]>> {
    return this.http.get<ApiResponse<StudentReturnDTO[]>>(`${this.baseUrl}/admin`);
  }

  getActiveStudents(): Observable<ApiResponse<StudentResponseDTO[]>> {
    return this.http.get<ApiResponse<StudentResponseDTO[]>>(`${this.baseUrl}/active`);
  }

  getActiveStudentsForAdmin(): Observable<ApiResponse<StudentReturnDTO[]>> {
    return this.http.get<ApiResponse<StudentReturnDTO[]>>(`${this.baseUrl}/admin/active`);
  }

  getDeletedStudents(): Observable<ApiResponse<StudentReturnDTO[]>> {
    return this.http.get<ApiResponse<StudentReturnDTO[]>>(`${this.baseUrl}/deleted`);
  }

  getDeletedStudentsForAdmin(): Observable<ApiResponse<StudentResponseDTO[]>> {
    return this.http.get<ApiResponse<StudentResponseDTO[]>>(`${this.baseUrl}/admin/deleted`);
  }

  getStudentById(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  getStudentByIdForAdmin(id: number): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.get<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin/${id}`);
  }

  getDeletedStudentById(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  getDeletedStudentByIdForAdmin(id: number): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.get<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin/deleted/${id}`);
  }

  getStudentByEmail(email: string): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/email/${email}`);
  }

  getStudentByEmailForAdmin(email: string): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.get<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin/email/${email}`);
  }

  getDeletedStudentByEmail(email: string): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.get<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/deleted/email/${email}`);
  }

  getDeletedStudentByEmailForAdmin(email: string): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.get<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin/deleted/email/${email}`);
  }

  getAverageProfiles(deleted: boolean = false): Observable<ApiResponse<TestResultProfilesDTO>> {
    return this.http.get<ApiResponse<TestResultProfilesDTO>>(
      `${this.baseUrl}/average-profiles?deleted=${deleted}`
    );
  }

  // ===== CREATE =====
  createStudent(student: StudentCreateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.post<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}`, student);
  }

  createStudentForAdmin(student: StudentDTO): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.post<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin`, student);
  }

  // ===== UPDATE =====
  updateProfile(id: number, student: StudentUpdateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/profile/${id}`, student);
  }

  updateStudent(id: number, student: StudentCreateDTO): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/${id}`, student);
  }

  updateStudentForAdmin(id: number, student: StudentModifyDTO): Observable<ApiResponse<StudentReturnDTO>> {
    return this.http.put<ApiResponse<StudentReturnDTO>>(`${this.baseUrl}/admin/${id}`, student);
  }

  // ===== DELETE =====
  hardDeleteStudent(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  softDeleteStudent(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  restoreStudent(id: number): Observable<ApiResponse<StudentResponseDTO>> {
    return this.http.put<ApiResponse<StudentResponseDTO>>(`${this.baseUrl}/restore/${id}`, {});
  }
}
