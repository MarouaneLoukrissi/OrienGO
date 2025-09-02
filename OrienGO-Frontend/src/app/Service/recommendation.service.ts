import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecommendationResponseDTO } from '../model/dto/RecommendationResponse.dto';
import { StudentJobLinkRequestDto } from '../model/dto/StudentJobLinkRequest.dto';
import { StudentJobLinkResponseDto } from '../model/dto/StudentJobLinkResponse.dto';
import { StudentTrainingLinkRequestDto } from '../model/dto/StudentTrainingLinkRequest.dto';
import { StudentTrainingLinkResponseDto } from '../model/dto/StudentTrainingLinkResponse.dto';
import { ApiResponse } from '../model/ApiResponse';
import { LinkType } from '../model/enum/LinkType.enum';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {

  private readonly apiUrl = 'http://localhost:8888/api/recommendations';

  constructor(private http: HttpClient) {}

  // === JOB LINKS ===
  linkJob(request: StudentJobLinkRequestDto): Observable<ApiResponse<StudentJobLinkResponseDto>> {
    return this.http.post<ApiResponse<StudentJobLinkResponseDto>>(`${this.apiUrl}/link-job`, request);
  }

  unlinkJob(request: StudentJobLinkRequestDto): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.apiUrl}/unlink-job`, { body: request });
  }

  // === TRAINING LINKS ===
  linkTraining(request: StudentTrainingLinkRequestDto): Observable<ApiResponse<StudentTrainingLinkResponseDto>> {
    return this.http.post<ApiResponse<StudentTrainingLinkResponseDto>>(`${this.apiUrl}/link-training`, request);
  }

  unlinkTraining(request: StudentTrainingLinkRequestDto): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.apiUrl}/unlink-training`, { body: request });
  }

  getRecommendationsByLinkType(type: LinkType, studentId?: number): Observable<ApiResponse<RecommendationResponseDTO>> {
    let params = new HttpParams().set('type', type);
    if (studentId) {
      params = params.set('studentId', studentId.toString());
    }

    return this.http.get<ApiResponse<RecommendationResponseDTO>>(`${this.apiUrl}/by-link-type`, { params });
  }

  // === RECOMMENDATIONS ===
  fetchExistingRecommendations(testResultId: number, studentId?: number): Observable<ApiResponse<RecommendationResponseDTO>> {
    let params = new HttpParams().set('testResultId', testResultId.toString());
    if (studentId != null) {
      params = params.set('studentId', studentId.toString());
    }

    return this.http.get<ApiResponse<RecommendationResponseDTO>>(`${this.apiUrl}/fetch-existing`, { params });
  }

  processRecommendations(studentId: number, testResultId: number): Observable<ApiResponse<RecommendationResponseDTO>> {
    let params = new HttpParams()
      .set('studentId', studentId.toString())
      .set('testResultId', testResultId.toString());

    return this.http.post<ApiResponse<RecommendationResponseDTO>>(`${this.apiUrl}/process`, {}, { params });
  }

  // === HEALTH CHECK ===
  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
