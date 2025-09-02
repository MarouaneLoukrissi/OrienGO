// src/app/model/dto/student-job-link-response.dto.ts

import { LinkType } from "../enum/LinkType.enum";

export interface StudentJobLinkResponseDto {
  id: number;
  studentId: number;
  studentName?: string;
  jobId: number;
  jobTitle?: string;
  jobCompany?: string;
  type: LinkType;
  createdAt?: string; // ISO date string from backend
}
