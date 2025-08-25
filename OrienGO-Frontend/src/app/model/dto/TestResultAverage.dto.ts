import { Category } from "../enum/Category.enum";

export interface TestResultAverageDTO {
  dominantProfile: Category; // Category name as string (e.g., "REALISTIC")
  percentage: number;      // Percentage of dominance
}
