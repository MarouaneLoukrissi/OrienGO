import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { TestCreateDTO } from '../model/dto/TestCreateDTO';
import { TestResponseDTO } from '../model/dto/TestResponseDTO';
import { ApiResponse } from '../model/ApiResponse';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  private baseUrl = 'http://localhost:8888/api/tests';

  constructor(private http: HttpClient) {}

  private testSubject = new BehaviorSubject<TestResponseDTO | null>(null);
  public test$ = this.testSubject.asObservable();

  // GET all tests (non-deleted)
  getTests(): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}`);
  }

  // GET all deleted tests
  getDeletedTests(): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}/deleted`);
  }

  // GET test by id (non-deleted)
  getTestById(id: number): Observable<ApiResponse<TestResponseDTO>> {
    return this.http.get<ApiResponse<TestResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  // GET deleted test by id
  getDeletedTestById(id: number): Observable<ApiResponse<TestResponseDTO>> {
    return this.http.get<ApiResponse<TestResponseDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  // GET tests by student id
  getTestsByStudentId(studentId: number): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}/student/${studentId}`);
  }

  // GET deleted tests by student id
  getDeletedTestsByStudentId(studentId: number): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}/deleted/student/${studentId}`);
  }

  // GET tests by student id and status
  getTestsByStudentIdAndStatus(studentId: number, status: string): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}/student/${studentId}/status/${status}`);
  }

  // GET deleted tests by student id and status
  getDeletedTestsByStudentIdAndStatus(studentId: number, status: string): Observable<ApiResponse<TestResponseDTO[]>> {
    return this.http.get<ApiResponse<TestResponseDTO[]>>(`${this.baseUrl}/deleted/student/${studentId}/status/${status}`);
  }

  // POST create a new test
  createTest(dto: TestCreateDTO): Observable<ApiResponse<TestResponseDTO>> {
    return this.http.post<ApiResponse<TestResponseDTO>>(`${this.baseUrl}`, dto)
    .pipe(
      tap(response => {
        if (response.data) {
          this.testSubject.next(response.data);
        }
      })
    );
  }

  // DELETE hard delete
  hardDeleteTest(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  // DELETE soft delete
  softDeleteTest(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  // PUT restore test
  restoreTest(id: number): Observable<ApiResponse<TestResponseDTO>> {
    return this.http.put<ApiResponse<TestResponseDTO>>(`${this.baseUrl}/restore/${id}`, {});
  }

  // POST save uncompleted test
  saveUncompletedTest(dto: any): Observable<ApiResponse<TestResponseDTO>> {
    return this.http.post<ApiResponse<TestResponseDTO>>(`${this.baseUrl}/save`, dto);
  }
}
