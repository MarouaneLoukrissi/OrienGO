// src/app/model/dto/student-training-link-response.dto.ts

import { LinkType } from "../enum/LinkType.enum";

export interface StudentTrainingLinkResponseDto {
  id: number;
  studentId: number;
  trainingId: number;
  type: LinkType;
  createdAt?: string; // ISO date string
}
