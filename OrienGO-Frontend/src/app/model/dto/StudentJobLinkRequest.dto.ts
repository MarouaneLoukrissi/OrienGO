// src/app/model/dto/student-job-link-request.dto.ts

import { LinkType } from "../enum/LinkType.enum";

export interface StudentJobLinkRequestDto {
  studentId: number;
  jobId: number;
  type: LinkType;
}
