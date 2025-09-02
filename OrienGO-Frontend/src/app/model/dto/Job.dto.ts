import { JobCategory } from "../enum/JobCategory.enum";
import { LinkType } from "../enum/LinkType.enum";

// job.dto.ts
export interface JobDTO {
  id?: number;

  title: string;
  description: string;
  category: JobCategory;

  education?: string;
  salaryRange?: string;
  jobMarket?: string;

  // RIASEC attributes
  riasecRealistic?: number;
  riasecInvestigative?: number;
  riasecArtistic?: number;
  riasecSocial?: number;
  riasecEnterprising?: number;
  riasecConventional?: number;

  tags?: string[];
  matchPercentage?: number;

  active: boolean;
  softDeleted: boolean;
  version?: number;
  jobRecommendationId: number;
  linkType: LinkType[],
  createdAt: string
}
