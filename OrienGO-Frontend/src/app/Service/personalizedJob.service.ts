import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { PersonalizedJobResponseDto } from '../model/dto/PersonalizedJobResponse.dto';
import { PersonalizedJobRequestDto } from '../model/dto/PersonalizedJobRequest.dto';
import { LinkType } from '../model/enum/LinkType.enum';

@Injectable({
  providedIn: 'root'
})
export class PersonalizedJobService {
  private baseUrl = 'http://localhost:8888/api/personalized-job';

  constructor(private http: HttpClient) {}

  // GET all jobs
  getAll(): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.get<ApiResponse<PersonalizedJobResponseDto[]>>(`${this.baseUrl}`);
  }

  // GET by id
  getById(id: number): Observable<ApiResponse<PersonalizedJobResponseDto>> {
    return this.http.get<ApiResponse<PersonalizedJobResponseDto>>(`${this.baseUrl}/${id}`);
  }

  // GET by student id
  getByStudentId(studentId: number): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.get<ApiResponse<PersonalizedJobResponseDto[]>>(`${this.baseUrl}/student/${studentId}`);
  }

  linkJobToStudent(
    studentId: number,
    personalizedJobId: number,
    type: LinkType
  ): Observable<ApiResponse<string>> {
    const body = { studentId, personalizedJobId, type };
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/link`, body);
  }

  unlinkJobFromStudent(
    studentId: number,
    personalizedJobId: number,
    type: LinkType
  ): Observable<ApiResponse<string>> {
    const body = { studentId, personalizedJobId, type };
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/unlink`, { body });
  }

  // GET jobs by link type (and optional studentId)
  getByLinkType(type: LinkType, studentId?: number): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    let params = new HttpParams().set('type', type);
    if (studentId !== undefined) {
      params = params.set('studentId', studentId.toString());
    }

    return this.http.get<ApiResponse<PersonalizedJobResponseDto[]>>(
      `${this.baseUrl}/by-link-type`,
      { params }
    );
  }

  // POST create
  create(request: PersonalizedJobRequestDto): Observable<ApiResponse<PersonalizedJobResponseDto>> {
    return this.http.post<ApiResponse<PersonalizedJobResponseDto>>(`${this.baseUrl}`, request);
  }

  // PUT update
  update(id: number, request: PersonalizedJobRequestDto): Observable<ApiResponse<PersonalizedJobResponseDto>> {
    return this.http.put<ApiResponse<PersonalizedJobResponseDto>>(`${this.baseUrl}/${id}`, request);
  }

  // DELETE
  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  // GET by job recommendation id
  getByJobRecommendationId(jobRecommendationId: number): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.get<ApiResponse<PersonalizedJobResponseDto[]>>(`${this.baseUrl}/job-recommendation/${jobRecommendationId}`);
  }

  // GET highlighted jobs
  getHighlighted(): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.get<ApiResponse<PersonalizedJobResponseDto[]>>(`${this.baseUrl}/highlighted`);
  }

  /** Fetch jobs by a list of jobRecommendationIds */
  getByJobRecommendationIds(studentId: number, jobRecommendationIds: number[]): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.post<ApiResponse<PersonalizedJobResponseDto[]>>(
      `${this.baseUrl}/by-job-recommendations/${studentId}`,
      jobRecommendationIds
    );
  }

  // POST by job recommendation ids for coach (no student-specific links)
  getByJobRecommendationIdsForCoach(jobRecommendationIds: number[]): Observable<ApiResponse<PersonalizedJobResponseDto[]>> {
    return this.http.post<ApiResponse<PersonalizedJobResponseDto[]>>(
      `${this.baseUrl}/by-job-recommendations/coach`,
      jobRecommendationIds
    );
  }

  // POST scrape & create
  scrapeAndCreate(jobRecommendationIds: number[], studentId: number): Observable<ApiResponse<Record<number, PersonalizedJobResponseDto[]>>> {
    let params = new HttpParams()
      .set('studentId', studentId.toString());
    jobRecommendationIds.forEach(id => {
      params = params.append('jobRecommendationIds', id.toString());
    });

    return this.http.post<ApiResponse<Record<number, PersonalizedJobResponseDto[]>>>(
      `${this.baseUrl}/scrape`,
      null, // body is null, since controller expects @RequestParam not @RequestBody
      { params }
    );
  }
}
