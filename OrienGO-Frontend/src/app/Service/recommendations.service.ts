import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { RecommendationsResponse } from '../interfaces/career.interface';

@Injectable({
  providedIn: 'root'
})
export class RecommendationsService {
  private apiUrl = 'http://localhost:3000/api'; // Replace with your backend URL

  constructor() {}

  // This will be replaced with actual HTTP calls to your backend
  getRecommendations(userId: string): Observable<RecommendationsResponse> {
    // Mock data - replace with actual HTTP call
    const mockData: RecommendationsResponse = {
      careers: [
        {
          id: '1',
          title: 'Psychologist',
          category: 'Health & Social',
          categoryColor: 'text-orange-700 bg-orange-100',
          description: 'Psychological support for patients',
          education: "Master's in Psychology",
          salary: '30 000 - 50 000$/year',
          jobMarket: 'High demand',
          matchPercentage: 66,
          skills: ['Listen', 'Together', 'Help']
        },
        {
          id: '2',
          title: 'Teacher',
          category: 'Education',
          categoryColor: 'text-orange-700 bg-orange-100',
          description: 'Knowledge transmission and student guidance',
          education: 'Master MEEF (Teaching)',
          salary: '25 000 - 45 000$/year',
          jobMarket: 'Stable',
          matchPercentage: 66,
          skills: ['Philippine', 'Communication', 'Pedagogy']
        }
      ],
      educationPaths: [
        {
          id: '1',
          title: 'University - Humanities',
          duration: '3-5 years',
          description: 'Theoretical training and research',
          matchPercentage: 89,
          specializations: ['Psychology', 'Sociology', 'History']
        },
        {
          id: '2',
          title: 'Engineering School',
          duration: '5 years',
          description: 'Complete training in science and technology',
          matchPercentage: 77,
          specializations: ['Civil Engineering', 'Electronics', 'Computer Science']
        },
        {
          id: '3',
          title: 'Art/Design School',
          duration: '3-5 years',
          description: 'Artistic and creative training',
          matchPercentage: 66,
          specializations: ['Graphic Design', 'Architecture', 'Applied Arts']
        },
        {
          id: '4',
          title: 'Business School',
          duration: '3-5 years',
          description: 'Training in management and business',
          matchPercentage: 33,
          specializations: ['Marketing', 'Finance', 'Management']
        }
      ],
      userProfile: {
        id: userId,
        riasecProfile: 'RIASEC'
      }
    };

    return of(mockData);
  }

  // Future method for actual HTTP calls
  /*
  getRecommendations(userId: string): Observable<RecommendationsResponse> {
    return this.http.get<RecommendationsResponse>(`${this.apiUrl}/recommendations/${userId}`);
  }

  updateUserPreferences(userId: string, preferences: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/users/${userId}/preferences`, preferences);
  }

  getCareerDetails(careerIds: string[]): Observable<Career[]> {
    return this.http.post<Career[]>(`${this.apiUrl}/careers/details`, { careerIds });
  }
  */
}
