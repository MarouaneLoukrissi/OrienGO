import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'admin' | 'superAdmin' | 'student' | 'coach';
  avatar?: string;
}

export interface MenuItem {
  id: string;
  label: string;
  icon: string;
  route?: string;
  isActive?: boolean;
  isHighlighted?: boolean;
  roles: ('admin' | 'superAdmin' | 'student' | 'coach')[];
  badge?: number;
  divider?: boolean; // For visual separation
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  //private currentUserSubject = new BehaviorSubject<User | null>(null);
  //public currentUser$ = this.currentUserSubject.asObservable();
  private currentUserSubject = new BehaviorSubject<User>({
    id: '1',
    name: 'Badr Icame',
    email: 'badricame@example.com',
    role: 'student' // Default role for demo
  });

  constructor(/*private http: HttpClient*/) {}

  /*fetchCurrentUser(): Observable<User> {
    return this.http.get<User>('/api/auth/me').pipe(
      tap(user => this.currentUserSubject.next(user))
    );
  }*/
  /*logout(): void {
    this.currentUserSubject.next(null);
    // maybe also call backend logout endpoint
  }*/

  public currentUser$ = this.currentUserSubject.asObservable();

  getCurrentUser(): User {
    return this.currentUserSubject.value;
  }

  setCurrentUser(user: User): void {
    this.currentUserSubject.next(user);
  }

  updateUserRole(role: 'admin' | 'superAdmin' | 'student' | 'coach'): void {
    const currentUser = this.getCurrentUser();
    this.setCurrentUser({ ...currentUser, role });
  }
}
