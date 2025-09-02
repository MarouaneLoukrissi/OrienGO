import { LinkType } from "../enum/LinkType.enum";
import { TrainingType } from "../enum/TrainingType.enum";

// training.dto.ts
export interface TrainingDTO {
  id?: number;
  name: string;
  type: TrainingType;
  description?: string;
  duration?: string;
  specializations?: string[];
  matchPercentage?: number;
  linkType: LinkType[],
  createdAt: string
}
