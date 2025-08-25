import { JobCategory } from "../enum/JobCategory.enum";

export interface JobRequestDto {
  title: string;
  description: string;
  category: JobCategory;
  education?: string;
  salaryRange?: string;
  jobMarket?: string;
  riasecRealistic?: number;
  riasecInvestigative?: number;
  riasecArtistic?: number;
  riasecSocial?: number;
  riasecEnterprising?: number;
  riasecConventional?: number;
  tags?: string[];
  softDeleted?: boolean;
}
