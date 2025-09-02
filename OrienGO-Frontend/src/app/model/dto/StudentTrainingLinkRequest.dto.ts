// src/app/model/dto/student-training-link-request.dto.ts

import { LinkType } from "../enum/LinkType.enum";

export interface StudentTrainingLinkRequestDto {
  studentId: number;
  trainingId: number;
  type: LinkType;
}
