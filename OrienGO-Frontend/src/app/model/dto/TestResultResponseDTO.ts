import { Category } from "../enum/Category";

export interface TestResultResponseDTO {
  id: number;
  testId: number;
  dominantType: Category;
  dominantTypeDescription: string;
  scores: { [category in Category]?: number }; // Map<Category, Double> → Record<Category, number>
  keyPoints: string;
  pdfId?: number; // optional if it may be null in Java
  shared: boolean;
  downloaded: boolean;
  softDeleted: boolean;
}
