export interface TestResultCreateDTO {
  testId: number; // Long → number
  answers: { [questionId: number]: number }; // Map<Long, Integer> → Record<number, number>
  durationMinutes: number; // Integer → number
}