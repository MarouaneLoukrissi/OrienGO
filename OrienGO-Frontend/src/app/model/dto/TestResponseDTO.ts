// src/app/model/dto/TestResponseDTO.ts
import { TestType } from '../enum/TestType';
import { TestStatus } from '../enum/TestStatus';

export interface TestResponseDTO {
  id: number;
  studentId: number;
  type: TestType;
  status: TestStatus;
  startedAt: string;       // ISO date string from backend
  completedAt: string;     // ISO date string from backend
  updatedAt: string;     // ISO date string from backend
  durationMinutes: number;
  questionsCount: number;
  answeredQuestionsCount: number;
  softDeleted: boolean;
}
