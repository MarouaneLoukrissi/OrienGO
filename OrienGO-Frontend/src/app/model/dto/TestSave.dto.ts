import { TestStatus } from "../enum/TestStatus.enum";

export interface TestSaveDTO {
  testId: number;
  answers: { [key: number]: number };
  durationMinutes: number;
  status: TestStatus,
  completedAt?: string | null;   // ISO date-time string from backend
  answeredQuestionsCount: number;
}