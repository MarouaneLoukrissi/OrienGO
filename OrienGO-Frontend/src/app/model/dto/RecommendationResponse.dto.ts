// recommendation-response.dto.ts

import { JobDTO } from "./Job.dto";
import { TrainingDTO } from "./Training.dto";

export interface RecommendationResponseDTO {
  jobs: JobDTO[];
  trainings: TrainingDTO[];
}
