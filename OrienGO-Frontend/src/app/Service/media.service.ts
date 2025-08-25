import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { TestCountDTO } from '../model/dto/TestCount.dto';


export interface MediaResponseDTO {
  id: number;
  fileName: string;
  type: string;
  url: string;
  userId: number;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  private baseUrl = 'http://localhost:8888/api/media';

  constructor(private http: HttpClient) {}

  // 1. Get all media
  getAllMedia(): Observable<ApiResponse<MediaResponseDTO[]>> {
    return this.http.get<ApiResponse<MediaResponseDTO[]>>(this.baseUrl);
  }

  // 2. Get media by ID
  getMediaById(id: number): Observable<ApiResponse<MediaResponseDTO>> {
    return this.http.get<ApiResponse<MediaResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  // 3. Get media by user ID
  getMediaByUserId(userId: number): Observable<ApiResponse<MediaResponseDTO[]>> {
    return this.http.get<ApiResponse<MediaResponseDTO[]>>(`${this.baseUrl}/user/${userId}`);
  }

  getLatestMediaByUserId(userId: number): Observable<ApiResponse<MediaResponseDTO[]>> {
    return this.http.get<ApiResponse<MediaResponseDTO[]>>(`${this.baseUrl}/user/latest/${userId}`);
  }

    // 4. Create media (upload file + metadata)
  createMedia(formData: FormData): Observable<ApiResponse<MediaResponseDTO>> {
    return this.http.post<ApiResponse<MediaResponseDTO>>(this.baseUrl, formData);
  }


  // 5. Update media
  updateMedia(id:number, formData: FormData): Observable<ApiResponse<MediaResponseDTO>> {
    return this.http.put<ApiResponse<MediaResponseDTO>>(`${this.baseUrl}/${id}`, formData);
  }

  // 6. Delete media
  deleteMedia(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  // 7. Download / display media file
  getMediaFileById(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/file/${id}`, { responseType: 'blob' });
  }
}
