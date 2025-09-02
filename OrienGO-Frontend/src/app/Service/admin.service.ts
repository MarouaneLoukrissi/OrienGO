import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { AdminReturnDTO } from '../model/dto/AdminReturn.dto';
import { AdminResponseDTO } from '../model/dto/AdminResponse.dto';
import { AdminCreateDTO } from '../model/dto/AdminCreate.dto';
import { AdminDTO } from '../model/dto/Admin.dto';
import { AdminUpdateDTO } from '../model/dto/AdminUpdate.dto';
import { AdminModifyDTO } from '../model/dto/AdminModify.dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8888/api/admin';

  constructor(private http: HttpClient) { }

  // Get all admins
  getAdmins(): Observable<ApiResponse<AdminReturnDTO[]>> {
    return this.http.get<ApiResponse<AdminReturnDTO[]>>(this.baseUrl);
  }

  // Get active admins
  getActiveAdmins(): Observable<ApiResponse<AdminReturnDTO[]>> {
    return this.http.get<ApiResponse<AdminReturnDTO[]>>(`${this.baseUrl}/active`);
  }

  // Get deleted admins
  getDeletedAdmins(): Observable<ApiResponse<AdminReturnDTO[]>> {
    return this.http.get<ApiResponse<AdminReturnDTO[]>>(`${this.baseUrl}/deleted`);
  }

  // Get admin by ID
  getAdminById(id: number): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.get<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/${id}`);
  }

  // Get deleted admin by ID
  getDeletedAdminById(id: number): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.get<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  // Get admin by email
  getAdminByEmail(email: string): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.get<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/email/${email}`);
  }

  // Get deleted admin by email
  getDeletedAdminByEmail(email: string): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.get<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/deleted/email/${email}`);
  }

  // Soft delete admin
  softDeleteAdmin(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  // Hard delete admin
  hardDeleteAdmin(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  // Restore deleted admin
  restoreAdmin(id: number): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.put<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/restore/${id}`, {});
  }

  // Create admin (basic)
  createAdmin(admin: AdminCreateDTO): Observable<ApiResponse<AdminResponseDTO>> {
    return this.http.post<ApiResponse<AdminResponseDTO>>(this.baseUrl, admin);
  }

  // Create admin (all fields)
  createAdminAllFields(admin: AdminDTO): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.post<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/all-fields`, admin);
  }

  // Update admin (basic)
  updateAdmin(id: number, admin: AdminUpdateDTO): Observable<ApiResponse<AdminResponseDTO>> {
    return this.http.put<ApiResponse<AdminResponseDTO>>(`${this.baseUrl}/${id}`, admin);
  }

  // Update admin (all fields)
  updateAdminAllFields(id: number, admin: AdminModifyDTO): Observable<ApiResponse<AdminReturnDTO>> {
    return this.http.put<ApiResponse<AdminReturnDTO>>(`${this.baseUrl}/${id}/all-fields`, admin);
  }
}
