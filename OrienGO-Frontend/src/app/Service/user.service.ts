
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { UserResponseDTO } from '../model/dto/UserRespone.dto';
import { UserCreateDTO } from '../model/dto/UserCreate.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8888/api/user';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<ApiResponse<UserResponseDTO[]>> {
    return this.http.get<ApiResponse<UserResponseDTO[]>>(`${this.baseUrl}`);
  }

  getDeletedUsers(): Observable<ApiResponse<UserResponseDTO[]>> {
    return this.http.get<ApiResponse<UserResponseDTO[]>>(`${this.baseUrl}/deleted`);
  }

  getUserById(id: number): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.get<ApiResponse<UserResponseDTO>>(`${this.baseUrl}/${id}`);
  }

  getDeletedUserById(id: number): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.get<ApiResponse<UserResponseDTO>>(`${this.baseUrl}/deleted/${id}`);
  }

  getUserByEmail(email: string): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.get<ApiResponse<UserResponseDTO>>(`${this.baseUrl}/email/${email}`);
  }

  getDeletedUserByEmail(email: string): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.get<ApiResponse<UserResponseDTO>>(`${this.baseUrl}/deleted/email/${email}`);
  }

  getUsersByRole(role: string): Observable<ApiResponse<UserResponseDTO[]>> {
    return this.http.get<ApiResponse<UserResponseDTO[]>>(`${this.baseUrl}/role/${role}`);
  }

  getDeletedUsersByRole(role: string): Observable<ApiResponse<UserResponseDTO[]>> {
    return this.http.get<ApiResponse<UserResponseDTO[]>>(`${this.baseUrl}/deleted/role/${role}`);
  }

  hardDeleteUser(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/hard/${id}`);
  }

  softDeleteUser(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }

  createUser(user: UserCreateDTO): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.post<ApiResponse<UserResponseDTO>>(`${this.baseUrl}`, user);
  }

  updateUser(id: number, user: UserCreateDTO): Observable<ApiResponse<UserResponseDTO>> {
    return this.http.put<ApiResponse<UserResponseDTO>>(`${this.baseUrl}/${id}`, user);
  }

  countUsersByRoles(deleted: boolean = false, roles: string[]): Observable<ApiResponse<number>> {
    const params = new URLSearchParams();
    params.set('deleted', String(deleted));
    roles.forEach(role => params.append('roles', role));

    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/count-by-roles?${params.toString()}`);
  }
}
