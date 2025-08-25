import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { JobResponseDto } from '../model/dto/JobResponse.dto';
import { JobRequestDto } from '../model/dto/JobRequest.dto';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  private baseUrl = 'http://localhost:8888/api/job';

  constructor(private http: HttpClient) { }

  getAllJobs(): Observable<ApiResponse<JobResponseDto[]>> {
    return this.http.get<ApiResponse<JobResponseDto[]>>(this.baseUrl);
  }

  countJobs(): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/count`);
  }

  getJobById(id: number): Observable<ApiResponse<JobResponseDto>> {
    return this.http.get<ApiResponse<JobResponseDto>>(`${this.baseUrl}/${id}`);
  }

  createJob(job: JobRequestDto): Observable<ApiResponse<JobResponseDto>> {
    return this.http.post<ApiResponse<JobResponseDto>>(this.baseUrl, job);
  }

  updateJob(id: number, job: JobRequestDto): Observable<ApiResponse<JobResponseDto>> {
    return this.http.put<ApiResponse<JobResponseDto>>(`${this.baseUrl}/${id}`, job);
  }

  deleteJob(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  getJobsByCategory(category: string): Observable<ApiResponse<JobResponseDto[]>> {
    return this.http.get<ApiResponse<JobResponseDto[]>>(`${this.baseUrl}/category/${category}`);
  }

  searchJobsByTitle(title: string): Observable<ApiResponse<JobResponseDto[]>> {
    let params = new HttpParams().set('title', title);
    return this.http.get<ApiResponse<JobResponseDto[]>>(`${this.baseUrl}/search`, { params });
  }

  // Uncomment if you enable active jobs endpoint in the backend
  // getActiveJobs(): Observable<ApiResponse<JobResponseDto[]>> {
  //   return this.http.get<ApiResponse<JobResponseDto[]>>(`${this.baseUrl}/active`);
  // }
}
